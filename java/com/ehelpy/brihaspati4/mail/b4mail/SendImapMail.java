package com.ehelpy.brihaspati4.mail.b4mail;

import com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.net.InetAddress;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;

import static com.ehelpy.brihaspati4.mail.b4mail.folders.B4FolderCreation.dirMake;

interface GlobalC
{
    String tempFileName="Send.eml";
    boolean flag1=false;
    boolean flag2=false;
    boolean flag3=false;
    boolean flag4=false;

    //boolean[] arr = {false, false, false, false};
}



public class SendImapMail implements GlobalC, FolderConstants {
    private static final Logger bmaillog = LogManager.getLogger(SendImapMail.class);
    String from = UserInfo.email;
    String  displayname = UserInfo.getUserName(from);

    static long start = System.nanoTime(); // used to calculate time in filename

    //Function which decides filenaming before storing a file
    public static String getFileformat() throws UnknownHostException {
        long ut1 = Instant.now().getEpochSecond();
        String epoch = Long.toString(ut1);
        //  System.out.println(epoch);
        File file = new File(tempFileName);
        if (!file.exists() || !file.isFile()) return epoch;
        //System.out.println((double)file.length()/1024); // inKB, for fragment calculation,

        InetAddress inetAddress = InetAddress.getLocalHost();
        String localIP = inetAddress.getHostAddress();
        // System.out.println("IP Address:- " + localIP);


        long end = System.nanoTime();
        long nanoseconds = end - start;
        System.out.println(nanoseconds);
        String filename = epoch + "-" + nanoseconds + "-" + file.length() + "-" + localIP + "-" + booleanToInt(flag1) + booleanToInt(flag2) + booleanToInt(flag3) + booleanToInt(flag4);
        return filename;
    }

    //Following function calculate MD5 for a file
    public static String getMD5(File eml) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(Paths.get(String.valueOf(eml)));
             DigestInputStream dis = new DigestInputStream(is, md)) {
            /* Read decorated stream (dis) to EOF as normal... */
        }
        //byte[] digest = md.digest();
        //String MD5hash = new String(md.digest(), StandardCharsets.UTF_8);
        String MD5hash = Base64.getEncoder().encodeToString(md.digest());
        return MD5hash;
    }

    //Following function calculate SHA256 for a file
    private static byte[] checksum(String filepath, MessageDigest md) throws IOException {

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        return md.digest();

    }

    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }


    //Following function converts a boolean to int i.e FALSE=0, TRUE=1
    public static int booleanToInt(boolean value) {
        // Convert true to 1 and false to 0.
        return value ? 1 : 0;
    }




    //Following function split a file by block size
    //https://www.admios.com/blog/how-to-split-a-file-using-java
    public static List<Path> splitFile(final String fileName, final int kbPerSplit) throws IOException {

        if (kbPerSplit <= 0) {
            throw new IllegalArgumentException("KB per Split must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1L * 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }
        }
        return partFiles;
    }


    // Following function is called from function b4mail.splitFile to write blocks of data to a file with extenstion .P1 .P2 etc.
    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel, List<Path> partFiles, int pos, String fn, long ns) throws IOException {
        //Path fileName = Paths.get(dir + UUID.randomUUID() + suffix);
        String suffix = ".P" + pos + "-" + ns;
        //System.out.println("fn is "+fn);
        Path nFileName = Paths.get(fn + "tmp" + File.separator + suffix);
        try (RandomAccessFile toFile = new RandomAccessFile(nFileName.toFile(), "rw");
             FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
        partFiles.add(nFileName);
    }

    // merge will be done at recipient end, it checks for all file fragments if received then merge
    // after merging the file wil be kept in new folder of INBOX e.g. /home/sm/B4/P2P/Maildir/new
    public static void mergeFiles(String srcDir, String destFile) throws IOException, NoSuchAlgorithmException {
        //This will merge files and save to INBOX
        bmaillog.trace("merging file from" + srcDir + " To file" + destFile);
        File directoryPath = new File(srcDir);
        File filesList[] = directoryPath.listFiles();
        int dirLen=filesList.length;
        String files=filesList[0].getName(); //get first file name
        int extractedLen=Integer.parseInt(files.substring(files.lastIndexOf("-") + 1)); // get the last number which indicates total files
        //System.out.println(extractedLen);
        if(extractedLen==dirLen-1) {
            bmaillog.trace("number of files in " + srcDir + " is equal to" +dirLen+" and file range is from 0-"+extractedLen);
            Scanner sc = null;
            FileWriter writer = new FileWriter(destFile);
            for (File file : filesList) {
                sc = new Scanner(file);
                String input;
                //      StringBuffer sb = new StringBuffer();
                while (sc.hasNextLine()) {
                    input = sc.nextLine();
                    writer.append(input + "\n");
                }
                writer.flush();
            }
            System.out.println("File merged");
            String fileHash1 = getMD5(new File(destFile));
            System.out.println(fileHash1);
        }
        else {
            bmaillog.trace("Failure while merging files from folder " + srcDir + ". Mismatch: files in dir is " +dirLen+" and expected is "+extractedLen);
        }
        deleteFile(new File(srcDir));   //delete the temp directory
    }

    public static void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }

    public static void ComposeEmail(Session session){
        String to = "soum@iitk.ac.in"; //TODO modify
        String sub= "Testing Attachment send via Java Mail API";
        String body="This is message body<html><b>Hi</b></html>";
        String attachmentfilename = "/home/sm/latex.zip";
        final String B4Encoding = "text/html;charset=UTF-8";
        String from = UserInfo.email;
        String  displayname = UserInfo.getUserName(from);
        try {
            // Create a default MimeMessage object.

            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(sub);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(body);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();

            DataSource source = new FileDataSource(attachmentfilename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachmentfilename);
            multipart.addBodyPart(messageBodyPart);

            message.addHeader("Content-Type", B4Encoding);
            message.addHeader("format", "flowed");
            message.setHeader("Content-Transfer-Encoding", "8bit");
            message.setSentDate(new Date());
            message.setReplyTo(new Address[]{new InternetAddress(from, displayname)});

            // Send the complete message parts
            message.setContent(multipart);
            // TODO: send.eml is a temporary file, need to handel locking issue when multiple compose window opens

            try (OutputStream out = new FileOutputStream(tempFileName)) {
                message.writeTo(out);
            }catch (FileNotFoundException fileNotFoundException){fileNotFoundException.printStackTrace();}
            String fileHash = getMD5(new File(tempFileName));
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = checksum(tempFileName, md);
            System.out.println(bytesToHex(hashInBytes));
            message.addHeader("X-Mail-hash", fileHash);
            System.out.println(fileHash);
            // second write to email file when hash is calculated.
            try (OutputStream out = new FileOutputStream(tempFileName)) {
                message.writeTo(out);
            }catch (FileNotFoundException fileNotFoundException){fileNotFoundException.printStackTrace();}

        } catch (MessagingException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



    public static Session CreateImapSession(){
        String from = UserInfo.email;
        final String username = UserInfo.getUserName(from);
        final String password = UserInfo.passwd;
        final String smtpserv = UserInfo.smtpserv;
        final String smtpport=UserInfo.smtpport;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpserv);
        props.put("mail.smtp.port", smtpport);
        props.put("mail.smtp.ssl.trust", smtpserv);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

    public static void MatchHashFromEmailHeader(Message message) throws MessagingException, IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        message.removeHeader("X-Mail-hash");
        try (OutputStream out = new FileOutputStream("Send1.eml")) {
            message.writeTo(out);
        }
        String fileHash1 = getMD5(new File("Send1.eml"));
        System.out.println(fileHash1);
        byte[] hashInBytes1 = checksum("Send1.eml", md);
        System.out.println(bytesToHex(hashInBytes1));
        // Send message
        //Transport.send(message);
        System.out.println("Sent message successfully....");
    }


    public static <Srring> void main(String[] args) throws IOException, NoSuchAlgorithmException {

        ComposeEmail(CreateImapSession());

            //move to outbox
            String lfile=getFileformat();
            String outFile = outboxpath + File.separator +lfile;
            File file = new File(tempFileName);
            if (file.renameTo(new File(outFile))) {
                System.out.println("Mail Saved to outbox");
                bmaillog.debug("Mail saved to outbox!");
            } else {
                System.out.println("File could not be saved: failed");
                bmaillog.debug("Directory is created!");
            }

            // We will now fragment the files
            String tempDir = outFile + "tmp";
            String tempOutFile = tmpInboxpath + File.separator + lfile; //destination file after merge
            dirMake(tempDir);
            splitFile(outFile, 512);
            mergeFiles(tempDir, tempOutFile);


    }

}
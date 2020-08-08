package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ehelpy.brihaspati4.mail.b4mail.folders.EmailFileName.*;
import static com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants.*;
import static com.ehelpy.brihaspati4.mail.b4mail.HashFuns.*;

public class ComposeB4Mail extends Service {
    private int result; //if sending is success/failure
    String tempFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    String getTempFilePath= tmpoutboxpath;
    String tempFileName=getTempFilePath+tempFile;

    private UserInfo aUser; //users account details
    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments = new ArrayList<File>(); // can have multiple attachments
    final String B4Encoding = "text/html;charset=UTF-8";

    public ComposeB4Mail(UserInfo aUser, String subject, String recipient, String content,
                         List<File> attachments) {
        super();
        this.aUser = aUser;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                try {
                    //SetUp SESSION, Create a MIME messge
                    Session session = aUser.getSession();
                    System.out.println("IN send" + session);
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(aUser.getEmailAddress());
                    message.addRecipients(Message.RecipientType.TO, recipient);
                    message.setSubject(subject);

                    //Setting the multipart mail body content:
                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);

                    message.addHeader("Content-Type", B4Encoding);
                    message.addHeader("format", "flowed");
                    message.setHeader("Content-Transfer-Encoding", "8bit");
                    message.setSentDate(new Date());
                    message.setReplyTo(new Address[]{new InternetAddress(aUser.getEmailAddress())});


                    //adding attachments:
                    if (attachments.size() > 0) {
                        for (File file : attachments) {
                            MimeBodyPart messageBodyPartAttach = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            messageBodyPartAttach.setDataHandler(new DataHandler(source));
                            messageBodyPartAttach.setFileName(file.getName());
                            multipart.addBodyPart(messageBodyPartAttach);
                        }
                    }
                    message.setContent(multipart);

                    try (OutputStream out = new FileOutputStream(tempFileName)) {
                        message.writeTo(out);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    String fileHash = getMD5(new File(tempFileName));
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashInBytes = checksum(tempFileName, md);
                    System.out.println(bytesToHex(hashInBytes));
                    message.addHeader("X-Mail-hash", fileHash);
                    System.out.println(fileHash);
                    // second write to email file when hash is calculated.
                    try (OutputStream out = new FileOutputStream(tempFileName)) {
                        message.writeTo(out);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                } catch (MessagingException | IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                getFileNameforSending(tempFileName);
                getFileNameWhileReceiving(tempFileName);//DUMMY will be chnaged

                //Sending the message:
                    /*
                    Transport transport = session.getTransport();
                    transport.connect(aUser.getProperties().getProperty("outgoingHost"),
                            aUser.getSmtpPort(),
                            aUser.getEmailAddress(),
                            aUser.getPassword());
                    //transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    */

                result = aUser.MESSAGE_SENT_OK;

                return result;
            }
        };
    }
}


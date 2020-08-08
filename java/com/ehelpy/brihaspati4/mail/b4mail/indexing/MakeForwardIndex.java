package com.ehelpy.brihaspati4.mail.b4mail.indexing;

import com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class MakeForwardIndex implements FolderConstants {
    private static final Logger b4maillog = LogManager.getLogger(MakeForwardIndex.class);
    static void getFolderlist(String dirName) throws IOException {
        File dir=new File(dirName);
        //filter to display only files
        FileFilter textFileFilter = new FileFilter(){
            public boolean accept(File file) {
                boolean isFile = file.isFile();
                if (isFile) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        //filter to display only folders
        FileFilter folderFilter = new FileFilter(){
            public boolean accept(File file) {
                boolean isFile = file.isFile();
                if (isFile) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        //get List of all the  files
        File folderList[] = dir.listFiles(folderFilter);
        b4maillog.debug("List of MailDirs are: ");
        for(File aDir : folderList) {
            if(aDir.getName().startsWith(".")){
                b4maillog.debug(aDir.getName());
                String dirl=aDir.getAbsolutePath();
                check4ForwardIndexFile(dirl);
                File subdir=new File(dirl+File.separator+curbox);
                File filesList[] = subdir.listFiles(textFileFilter);
                for(File file : filesList) {
                    populateIndex(file.getAbsolutePath());
                }
            }
        }
    }
    //check if index file is present else create it.
    //file name format: b4fw.foldername.index
    private static void check4ForwardIndexFile(String dirname) {
        String IndexFilename=dirname+File.separator+"b4fw"+dirname.substring(dirname.lastIndexOf(File.separator) + 1).trim()+".index";
        //System.out.println(dirname.substring(dirname.lastIndexOf(File.separator) + 1).trim());
        //System.out.println(IndexFilename);
        File ifile=new File(IndexFilename);
        if(ifile.exists() && !ifile.isDirectory()) {
            b4maillog.debug(ifile+" exists Doing nothing now");
        }else{
            try{
                boolean fvar = ifile.createNewFile();
                if (fvar){
                    b4maillog.debug(ifile+" created");
                }
                else{
                    b4maillog.debug(ifile+" exists Doing nothing now");
                }
            } catch (IOException e) {
                b4maillog.debug(ifile+"Exception Occurred while creating:");
                e.printStackTrace();
            }
        }

    }

    //populate email header info into indexfile
    private static void populateIndex(String aMailFile) throws IOException {
        System.out.println(aMailFile);
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = null;
        try {
            source = new FileInputStream(aMailFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MimeMessage message = null;
        try {
            message = new MimeMessage(mailSession, source);
            String to = Arrays.toString(message.getAllRecipients());
            String dt=dateFormatdisplay(message.getSentDate());
            String fileMetaData=aMailFile.substring(aMailFile.lastIndexOf(File.separator) + 1).trim()+":"+
                    message.getFrom()[0]+":"+
                    message.getAllRecipients()[0]+":"+
                    dt+":"+
                    message.getSubject()+":"+
                    to+":";
            System.out.println("metadata : " +fileMetaData);
            File file = new File("append.txt");
            FileWriter fr = new FileWriter(file, true);
            fr.write(fileMetaData);
            fr.close();
        } catch (MessagingException | ParseException e) {
            e.printStackTrace();
        }
        // append the string to file, for this FileWriter is used which is for less numberof write of text data
        // BufferedWriter is for very high number of writes
        // FileOutputStream is used  when it’s raw or binary data.



    }
    private static String dateFormatdisplay(Date S) throws ParseException {
        //String S="Thu May 28 04:20:07 IST 2020";
        //Date todaysDate = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(S);
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String str2 = df2.format(S);
        System.out.println("String in dd-MM-yyyy HH:mm:ss format is: " + str2);
        return str2;
    }

    public static void main(String args[]) throws IOException {
        getFolderlist(P2PmaildirPath);
    }

}

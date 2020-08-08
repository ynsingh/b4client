package com.ehelpy.brihaspati4.mail.b4mail;


import com.ehelpy.brihaspati4.mail.b4mail.folders.B4FolderCreation;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;
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
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;


public class GeneralTest {
    private static final Logger bmaillog = LogManager.getLogger(GeneralTest.class);
    public static void main(String[] args) throws IOException, MessagingException{

        //01. Check folder structure
        B4FolderCreation b4folders= new B4FolderCreation();
        b4folders.createB4Folders();

        //02. Check if a host and port is reachable
        System.out.println("mmtp port live?: "+hostAvailabilityCheck("mmtp.iitk.ac.in",25));


        //03. Email sending test
        /*String from = UserInfo.email;
        final String username = UserInfo.getUserName(from);
        final String password = UserInfo.passwd;
        final String smtpserv = UserInfo.smtpserv;
        final int smtpport= Integer.parseInt(UserInfo.smtpport);
        */
        String from = "mailadm@iitk.ac.in";
        final String username = "mailadm";
        final String password = "morapc11";
        final String smtpserv = "mmtp.iitk.ac.in";
        final int smtpport= 25;

        final String needAuth="true";
        final String encryptionType="TLS";
        Boolean status;

        status=smtphostAvailabilityCheck(smtpserv,smtpport,from,username,password,needAuth,encryptionType);

        System.out.println("mmtp smtp live?: "+status);
        //01. test file name creation
        //String filename = getFileNameFormat();
        //System.out.println("Filename in test class"+filename);
        // System.out.println(outboxpath);
        //02.compose a mail
    }



    public static boolean hostAvailabilityCheck(String host,int port) {
        try (Socket s = new Socket(host, port)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    public static Message composeTestMail(Session session,String from) {
        try {
            Message message = new MimeMessage(session);
            //set header
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.setHeader("Content-Transfer-Encoding", "8bit");
            //set mail info
            message.setFrom(new InternetAddress(from));
            message.setReplyTo(InternetAddress.parse(from, false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(from)); //send to self
            message.setSubject("Test mail");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Test mail body pl igonore");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();

            message.setSentDate(new Date());

            // Send the complete message parts
            message.setContent(multipart);
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static Session CreateSMTPSession(String host, int port,String uname, String password, String auth,String encType){
        Properties props = new Properties();
        if (auth.equals(true)) {
            props.setProperty("mail.smtp.auth", "true");
        } else {
            props.setProperty("mail.smtp.auth", "false");
        }
        if (encType.endsWith("TLS")) {
            props.setProperty("mail.smtp.starttls.enable", "true");
        } else if (encType.endsWith("SSL")) {
            props.setProperty("mail.smtp.ssl.enable", "true");
        }
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");


        Authenticator smtpauth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(uname, password);
            }
        };
        Session session = Session.getDefaultInstance(props, smtpauth);
        session.setDebug(true);
        return session;
    }


    public static boolean smtphostAvailabilityCheck(String host,int port, String from, String uname, String password, String auth,String encType) {
        boolean result = false;
        try{

            Session session = CreateSMTPSession(host,port,uname,password,auth,encType);
            System.out.println("Session info: "+session.toString());

            Message message = composeTestMail(session,from);
            System.out.println("Message info: "+message.toString());


            Transport transport = session.getTransport("smtp");
            System.out.println("Transport info: "+transport.toString());
            transport.connect(host, port, uname, password);
            //transport.connect();
            Transport.send(message,message.getAllRecipients());
            transport.close();
            result = true;
        } catch(MessagingException e) {
            bmaillog.trace(e.toString(), "SMTP: Messaging Exception Occurred", false, true);
        } catch (Exception e) {
            bmaillog.trace(e.toString(), "SMTP: Unknown Exception", false, true);
        }
        return result;
    }

}

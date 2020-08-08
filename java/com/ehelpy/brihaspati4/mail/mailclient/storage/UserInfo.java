/*
This file will read the property file and use those to do various email related operations.
This is independent of saving configuration data system.
 */
//Defaunct class
package com.ehelpy.brihaspati4.mail.mailclient.storage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;



import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import javafx.collections.ObservableList;

import javax.mail.Folder;
import javax.mail.Message;


public class UserInfo {
    public static String email = null;
    public static String passwd = null;
    public static String imapserv= null;
    public static String imapport= null;
    public static String smtpserv= null;
    public static String smtpport= null;

    public static final int LOGIN_STATE_NOT_READY = 0;
    public static final int LOGIN_STATE_FAILED_BY_NETWORK = 1;
    public static final int LOGIN_STATE_FAILED_BY_CREDENTIALS = 2;
    public static final int LOGIN_STATE_SUCCEDED = 3;

    //reply or forward indicator:
    public static final int REPLY_MESSAGE = 4;
    public static final int FORWARD_MESSAGE = 5;
    public static final int STANDALONE_MESSAGE = 6;

    //Sending result:
    public static  int MESSAGE_SENT_OK = 7;
    public static final int MESSAGE_SENT_ERROR = 8;

    public static Properties properties;
    public static Session session;
    public static Store store;
    //default status of log in state, The state is changed if successful login is done by store.connect (below)
    // EmailAccountControllerDemon uses this state info to fetch folder and mails
    private static int loginState = LOGIN_STATE_NOT_READY;

    public  String getEmailAddress() {
        return email;
    }
    public static String getPassword(){
        return passwd;
    }
    public static String getImapServ() {
        return imapserv;
    }
    public static String getImapPort(){
        return imapport;
    }
    public static String getSmtpServ() {
        return smtpserv;
    }
    public static int getSmtpPort(){
        return Integer.parseInt(smtpport.trim());
    }

    public static Properties getProperties(){
        return properties;
    }
    public static Store getStore() {
        return store;
    }
    public static Session getSession() {
        return session;
    }


    public static int getLoginState(){
        return loginState;
    }
    public static String getUserName(String email){
        String[] parts = email.split("@");
        System.out.println(parts[0]);
        return parts[0];
        //String user = parts[0];
        //String domain = parts[1];
        //System.out.println(user);
    }
    public UserInfo() {
        try {
            InputStream input = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            email = prop.getProperty("Email");
            passwd = prop.getProperty("Password");
            imapserv = prop.getProperty("IMAPHOST");
            smtpserv = prop.getProperty("SMTPHOST");
            imapport = prop.getProperty("IMAPORT");
            smtpport = prop.getProperty("SMTPPORT");


        } catch (IOException e) {
            e.printStackTrace();
        }
        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("incomingHost", imapserv);

        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", smtpserv);
        properties.put("mail.smtp.port", smtpport);
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", smtpserv);
        //properties.put("mail.smtp.socketFactory.fallback", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        String user=getUserName(email);
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //return new PasswordAuthentication(email, passwd); //for gmail
                return new PasswordAuthentication(user, passwd);
            }
        };

        // Session.getDefaultInstance() method creates mail session object using above SMTP properties
        session = Session.getInstance(properties, auth);
        //System.out.println("IN Userinfo"+session);
        try{
            // Create store(javax.mail.Store) to connect , read and retrieve messages from IMAP server
            //"this" is a reference to the current object store from Store class
            this.store = session.getStore();

            // Connect to IMAP server using imap server, user id, password
            store.connect(imapserv, user, passwd);
            //store.connect(imapserv, email, passwd); //for gmail
            loginState = LOGIN_STATE_SUCCEDED;
            System.out.println("EmailAccount constructed successfully: " + this+" LOGIN_STATE_SUCCEDED: "+LOGIN_STATE_SUCCEDED);
        }catch(AuthenticationFailedException ae){
            System.out.println("Not able to connect to store "+imapserv+" for email id "+email);
            ae.printStackTrace();
            loginState = LOGIN_STATE_FAILED_BY_CREDENTIALS;
        }catch(Exception e){
            System.out.println("Not able to auth IMAP server "+imapserv+" for email id "+email);
            e.printStackTrace();
            loginState = LOGIN_STATE_FAILED_BY_NETWORK;
        }

    }


    public static void addB4EmailsToIndex(ObservableList<MailStoreBeans> data){
        try {
            //open INBOX in IMAP server in R mode

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            int messageCount = folder.getMessageCount();

            // Read all the messages objects using a loop
            for(int i = messageCount; i > 0; i--){
                Message aMail = folder.getMessage(i);
                MailStoreBeans aMailBean = new MailStoreBeans(aMail.getSubject(), aMail.getFrom()[0].toString(),aMail.getReceivedDate(), aMail.getSize(), "",true,aMail);
                System.out.println("Got: " + aMailBean);
                data.add(aMailBean);

            }
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

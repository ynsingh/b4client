package com.ehelpy.brihaspati4.mail.b4mail;



import com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
/**
 * We will create in userhome/B4/MailDir
 */

public class SimpleDelivery implements GlobalC, FolderConstants {
    private static final Logger bmaillog = LogManager.getLogger(SimpleDelivery.class);
    public static void main(String args[]) throws Exception {
        // three steps to send email
        //01. Get the session object that stores all the information such as host name, username, password etc.
        //02. Compose the message
        //03. Send the message


        //01. javax.mail.Session class provides two methods to get the object of session, Session.getDefaultInstance() method and Session.getInstance() method.
        Properties p = new Properties();
        Session session = Session.getDefaultInstance(p);
        p.put("mail.store.maildir.autocreatedir", "true");

        /*
        String myDirectory = "B4"; // user Folder Name
        String path = getUsersHomeDir() + File.separator + myDirectory;

        if (new File(path).mkdir()) {
            System.out.println("Directory is created!");
        } else {
            System.out.println("Failed to create directory! Directory already present");
        }
        */

        //02. javax.mail.Message abstractclass and javax.mail.internet.MimeMessage class provides methods to compose the message,
        // 02. create message object mm
        MimeMessage mm = new MimeMessage(session);
        mm.setFrom(new InternetAddress());
        mm.setText("hello\nworld\n");



        //String mailDirPath = "maildir:///"+path+"/Maildir";
        //String url = mailDirPath;
        //System.out.println(url);
        Store store = session.getStore(new URLName(imappath));
        store.connect();




        Folder inbox = store.getFolder("inbox");
        inbox.appendMessages(new Message[]{mm});


        inbox.open(Folder.READ_WRITE);
        Message m = inbox.getMessage(1);
        m.writeTo(System.out);
        System.out.println("subject of this message: " + m.getSubject());
        m.writeTo(System.out);
    }

    public static String getUsersHomeDir() {
        String users_home = System.getProperty("user.home");
        return users_home.replace("\\", "/"); // to support all platforms.
    }
}
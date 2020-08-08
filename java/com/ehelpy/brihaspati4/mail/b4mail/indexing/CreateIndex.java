package com.ehelpy.brihaspati4.mail.b4mail.indexing;

import com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CreateIndex implements FolderConstants {
    public static void main(String args[]) throws Exception {
        System.out.printf("newoutboxpath");
        display(new File(newoutboxpath+"C:\\temp\\message.eml"));
    }
        public static void display(File emlFile) throws Exception{
            Properties props = System.getProperties();
            props.put("mail.host", "smtp.dummydomain.com");
            props.put("mail.transport.protocol", "smtp");

            Session mailSession = Session.getDefaultInstance(props, null);
            InputStream source = new FileInputStream(emlFile);
            MimeMessage message = new MimeMessage(mailSession, source);


            System.out.println("Subject : " + message.getSubject());
            System.out.println("From : " + message.getFrom()[0]);
            System.out.println("--------------");
            System.out.println("Body : " +  message.getContent());
        }
    }

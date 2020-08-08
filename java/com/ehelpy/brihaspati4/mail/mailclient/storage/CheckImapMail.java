package com.ehelpy.brihaspati4.mail.mailclient.storage;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.util.Properties;

public class CheckImapMail {

    public static void check(String host, String storeType, String user, String password) {
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "143");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("imap");

            store.connect(host, user, password);
            System.out.println("store is " + store);

            Folder[] folders = store.getDefaultFolder().list("*");
            for (Folder folder : folders) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                }
            }

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("Total messages: " + messages.length);
            System.out.println("messages count: " + emailFolder.getMessageCount());
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Receive Date: " + message.getReceivedDate());
                System.out.println("Flag: " + message.getFlags());
                System.out.println("Message Number: " + message.getMessageNumber());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Size: " + messages[i].getSize() + "bytes.");
                System.out.println(messages[i].getLineCount() + " lines.");
                Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
                //System.out.println("Text: " + message.getContent().toString());
         /*
         //get full header
          Enumeration headers = messages[i].getAllHeaders();
          while (headers.hasMoreElements()) {
              Header h = (Header) headers.nextElement();
              System.out.println(h.getName() + ": " + h.getValue());
          }
          */

         /*
          String disposition = messages[i].getDisposition();
          if (disposition == null){
              System.out.println("No attachments");
          }else if (disposition.equals(Part.INLINE)) {
              System.out.println("This part should be displayed inline");
          } else if (disposition.equals(Part.ATTACHMENT)) {
              System.out.println("This part is an attachment");
              String fileName = messages[i].getFileName();
              System.out.println("The file name of this attachment is " + fileName);
          }

          String description = messages[i].getDescription();
          if (description != null) {
              System.out.println("The description of this message is " + description);
          }
          */
                //Iterate recipients
                System.out.print("To: ");
                for (int j = 0; j < toAddress.length; j++) {
                    System.out.println(toAddress[j].toString());
                }
                //if a message contains attachments, its content must be of type MultiPart, and the part contains a file must be of type MimeBodyPart
                String contentType = message.getContentType();

                if (contentType.contains("multipart")) {
                    // this message may contain attachment
                    Multipart multiPart = (Multipart) message.getContent();

                    for (int k = 0; k < multiPart.getCount(); k++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(k);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            System.out.println("Attachment is " + part.getFileName());
                            String path = System.getProperty("user.home");
                            //part.saveFile(path+"/Desktop/" + part.getFileName()); // save the attachment

                        }
                    }
                }

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String host = "qasid.iitk.ac.in";// change accordingly
        String mailStoreType = "imap";
        String username = "mailadm";// change accordingly
        String password = "morapc11";// change accordingly

        check(host, mailStoreType, username, password);

    }

}

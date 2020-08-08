package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendImapEmailDemon extends Service<Integer> {
    private int result; //if sending is success/failure

    private UserInfo aUser; //users account details
    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments = new ArrayList<File>(); // can have multiple attachments

    public SendImapEmailDemon(UserInfo aUser, String subject, String recipient, String content,
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
        return new Task<Integer>(){
            @Override
            protected Integer call() throws Exception {
                try {
                    //SetUp SESSION, Create a MIME messge
                    Session session = aUser.getSession();
                    System.out.println("IN send"+session);
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(aUser.getEmailAddress());
                    message.addRecipients(Message.RecipientType.TO, recipient);
                    message.setSubject(subject);

                    //Setting the multipart mail body content:
                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);

                    //adding attachments:
                    if(attachments.size() >0){
                        for(File file: attachments){
                            MimeBodyPart messageBodyPartAttach = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            messageBodyPartAttach.setDataHandler(new DataHandler(source));
                            messageBodyPartAttach.setFileName(file.getName());
                            multipart.addBodyPart(messageBodyPartAttach);
                        }
                    }
                    message.setContent(multipart);

                    //Sending the message:
                    Transport transport = session.getTransport();
                    transport.connect(aUser.getProperties().getProperty("outgoingHost"),
                            aUser.getSmtpPort(),
                            aUser.getEmailAddress(),
                            aUser.getPassword());
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();

                    result = aUser.MESSAGE_SENT_OK;
                } catch (Exception e) {
                    result = aUser.MESSAGE_SENT_ERROR;
                    e.printStackTrace();
                }
                return result;
            }
        };
    }
}

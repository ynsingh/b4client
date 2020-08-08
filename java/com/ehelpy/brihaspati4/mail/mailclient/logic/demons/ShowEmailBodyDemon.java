/*
This classs will render the email body in one thread and display in main application thread.
 */
package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

public class ShowEmailBodyDemon extends Service<Void>{
    private MailStoreBeans messageToRender;
    private WebEngine messageBodyRendererEngine;

    private StringBuffer sb = new StringBuffer();


    public ShowEmailBodyDemon(WebEngine messageRendererEngine) {
        this.messageBodyRendererEngine = messageRendererEngine;
        this.setOnSucceeded(e->{showMessage();});
    }

    public void setMessageToRender(MailStoreBeans messageToRender){
        this.messageToRender = messageToRender;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                renderMessage();
                return null;
            }
        };
    }

    private void renderMessage(){
        //clear the sb:
        sb.setLength(0);
        Message message = messageToRender.getMessageReference();
        try {
            String messageType = message.getContentType();
            if(messageType.contains("TEXT/HTML") ||
                    messageType.contains("TEXT/PLAIN") ||
                    messageType.contains("text")){
                sb.append(message.getContent().toString());
            } else if(messageType.contains("multipart")){
                Multipart mp = (Multipart)message.getContent();
                for (int i = mp.getCount()-1; i >= 0; i--) {
                    BodyPart bp = mp.getBodyPart(i);
                    String contentType = bp.getContentType();
                    if(contentType.contains("TEXT/HTML") || contentType.contains("TEXT/PLAIN") ||
                            contentType.contains("mixed")|| contentType.contains("text")){
                        //Here the risk of incomplete messages are shown, for messages that contain both
                        //html and text content, but these messages are very rare;
                        if (sb.length()== 0) {
                            sb.append(bp.getContent().toString());
                        }
                        //here the attachments are handled TODO by someone who cares
                    }else if(contentType.toLowerCase().contains("application")){
                        MimeBodyPart mbp = (MimeBodyPart)bp;

                        //Sometimes the text content of the message is encapsulated in another multipart,
                        //so we have to iterate again through it.
                    }else if(bp.getContentType().contains("multipart")){
                        Multipart mp2 = (Multipart)bp.getContent();
                        for (int j = mp2.getCount()-1; j >= 0; j--) {
                            BodyPart bp2 = mp2.getBodyPart(i);
                            if((bp2.getContentType().contains("TEXT/HTML") ||
                                    bp2.getContentType().contains("TEXT/PLAIN") ) ){
                                sb.append(bp2.getContent().toString());
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Exception while visualizing message: ");
            e.printStackTrace();
        }
    }
    /**
     * Only call once the message is loaded!!!
     * handle the info about attachments
     */
    private void showMessage(){
        messageBodyRendererEngine.loadContent(sb.toString());
    }

}

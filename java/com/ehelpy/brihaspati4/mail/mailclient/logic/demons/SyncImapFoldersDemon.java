/*
This class gets IMAP folders from server
 */
package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.logic.AccessStore;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;
import com.ehelpy.brihaspati4.mail.mailclient.storage.folder.EmailFolderBean;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.security.PrivateKey;

public class SyncImapFoldersDemon<Private> extends Service<Void> {
    private EmailFolderBean foldersRoot;
    private UserInfo emailAccountBean;
    private AccessStore imapStore;
    private static int numberOfSyncFolderActiveDemons = 0;  //counts how many instance of this service running

    public SyncImapFoldersDemon(EmailFolderBean foldersRoot, UserInfo emailAccountBean, AccessStore imapStore) {
        this.foldersRoot = foldersRoot;
        this.emailAccountBean = emailAccountBean;
        this.imapStore=imapStore;
        this.setOnSucceeded(e->{    // FLAG from service worker interface
            System.out.println("Removing service");
            numberOfSyncFolderActiveDemons--;   //on success of thread completion
        });
    }


    @Override
    protected Task<Void> createTask() {
        if (this.getState().toString() != "READY") {
            numberOfSyncFolderActiveDemons++;
            System.out.println("Status is"+this.getState()+"incremented counter to: "+numberOfSyncFolderActiveDemons);
        }
        if (emailAccountBean != null) {
            try {
                Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();
                for (Folder folder : folders) {
                    AccessStore.addFolder(folder);
                    EmailFolderBean item = new EmailFolderBean(folder.getName(), folder.getFullName());
                    foldersRoot.getChildren().add(item);
                    item.setExpanded(true);
                    newMailListenerinFolder(folder, item);
                    SyncImapFolderEmailDemon syncMessagesOnFolder = new SyncImapFolderEmailDemon(item, folder);//which folderbean, 2nd which folder
                    syncMessagesOnFolder.start();
                    System.out.println("added: " + folder.getName());
                    //Following section is for subfolders
                    Folder[] subFolders = folder.list();
                    for (Folder subFolder : subFolders) {
                        AccessStore.addFolder(subFolder);
                        EmailFolderBean subItem = new EmailFolderBean(subFolder.getName(), subFolder.getFullName());
                        item.getChildren().add(subItem);
                        newMailListenerinFolder(subFolder, subItem);
                        SyncImapFolderEmailDemon syncMessagesOnSubFolder = new SyncImapFolderEmailDemon(subItem, subFolder);
                        syncMessagesOnSubFolder.start();
                        System.out.println("added: " + subFolder.getName());

                    }
                }
            } catch (MessagingException err) {
                err.printStackTrace();
            }
        }
        return null;
    }
    //Following method listens to new messages in a folder
    //uses  Adapter pattern (acts as a connector between two incompatible interfaces) for java mail api
    // MessageCountEvent listens for all new mails, and stores them.
    private void newMailListenerinFolder(Folder folder, EmailFolderBean item) {
        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for (int i = 0; i < e.getMessages().length; i++) {
                    try {
                        Message currentMessage = folder.getMessage(folder.getMessageCount() - i);
                        item.addEmail(0, currentMessage);// new mail is added to top of list (at index 0)
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    public static boolean WhenNoActiveFolderSyncDemonsRunning(){
        return numberOfSyncFolderActiveDemons == 0; //return true if no folder sync service running
    }
}


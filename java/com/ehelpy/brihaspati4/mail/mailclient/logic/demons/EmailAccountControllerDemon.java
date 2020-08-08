package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.logic.AccessStore;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;
import com.ehelpy.brihaspati4.mail.mailclient.storage.folder.EmailFolderBean;

public class EmailAccountControllerDemon extends Service<Integer> {
    private EmailFolderBean rootDir;
    private String accountType;
    private AccessStore imapstore;

    public EmailAccountControllerDemon( EmailFolderBean rootDir, AccessStore imapstore) {
        this.rootDir = rootDir;
        this.imapstore = imapstore;
    }


    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                UserInfo imapAccount = new UserInfo();
                    if (imapAccount.getLoginState() == imapAccount.LOGIN_STATE_SUCCEDED) {

                        EmailFolderBean emailFolders = new EmailFolderBean(imapAccount.email);
                        rootDir.getChildren().add(emailFolders);


                        EmailFolderBean Imap = new EmailFolderBean<>("IMAP");
                        emailFolders.getChildren().add(Imap);

                        EmailFolderBean B4 = new EmailFolderBean<>("B4");
                        emailFolders.getChildren().add(B4);

                        AccessStore.addAccount(imapAccount);

                        EmailFolderBean B4Inbox=new EmailFolderBean<>("Inbox", "CompleteInbox");
                        EmailFolderBean B4Sent = new EmailFolderBean<>("Sent", "CompleteSent");
                        EmailFolderBean B4Drafts = new EmailFolderBean<>("Drafts", "CompleteDrafts");
                        EmailFolderBean B4Outbox = new EmailFolderBean<>("Outbox", "CompleteOutbox");
                        EmailFolderBean B4Spam = new EmailFolderBean<>("Spam", "CompleteSpam");
                        EmailFolderBean B4Trash = new EmailFolderBean<>("Trash", "CompleteTrash");
                        EmailFolderBean B4Archive = new EmailFolderBean<>("Archive", "CompleteArchive");
                        B4.getChildren().addAll(B4Inbox,B4Sent,B4Drafts,B4Outbox,B4Spam,B4Trash,B4Archive);



                        //rootDir.getChildren().add(emailFolderBean);
                        // EmailFolderBean Imap = new EmailFolderBean<>(accountType);
                        // Imap.getChildren().add(Imap);
                        // FetchImapFolderDemon imapFolders = new FetchImapFolderDemon(Imap, imapAccount);
                        SyncImapFoldersDemon imapFolders = new SyncImapFoldersDemon(Imap, imapAccount, imapstore);
                        imapFolders.start();
                    }
                return imapAccount.getLoginState();
            }

        };
    }


}

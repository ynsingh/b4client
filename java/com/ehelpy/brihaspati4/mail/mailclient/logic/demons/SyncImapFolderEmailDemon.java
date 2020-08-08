/* This class will sync all imap folders for an email */

package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.storage.folder.EmailFolderBean;

import javax.mail.Folder;
import javax.mail.Message;

public class SyncImapFolderEmailDemon extends Service {

    private EmailFolderBean emailFolder;
    private Folder folder;

    public SyncImapFolderEmailDemon(EmailFolderBean emailFolder, Folder folder) {
        this.emailFolder = emailFolder;
        this.folder = folder;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                if(folder.getType() != Folder.HOLDS_FOLDERS){
                    folder.open(Folder.READ_WRITE);
                }
                int folderSize = folder.getMessageCount(); // number of messages in a folder
                for(int i = folderSize; i > 0; i--){
                    Message currentMessage = folder.getMessage(i);
                    emailFolder.addEmail(-1, currentMessage);
                }
                return null;
            }
        };
    }
}

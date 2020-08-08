package com.ehelpy.brihaspati4.mail.mailclient.logic.demons;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class PeriodicFolderMsgCountCheckDemon extends Service<Void> {
    private List<Folder> foldersList;
    public PeriodicFolderMsgCountCheckDemon(List<Folder> foldersList) {
        this.foldersList = foldersList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                for(;;){
                    try {
                        Thread.sleep(20000);//every 10 second folders will be checked
                        System.out.println(SyncImapFoldersDemon.WhenNoActiveFolderSyncDemonsRunning()+"CHECKING FOR EMAIL!!!");
                        if (SyncImapFoldersDemon.WhenNoActiveFolderSyncDemonsRunning()) {
                            System.out.println(foldersList+"CHECKING FOR EMAILS!!!");
                            for (Folder folder : foldersList) {
                                if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
                                    folder.getMessageCount();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}

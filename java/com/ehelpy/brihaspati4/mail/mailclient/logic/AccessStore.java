package com.ehelpy.brihaspati4.mail.mailclient.logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;
import com.ehelpy.brihaspati4.mail.mailclient.storage.folder.EmailFolderBean;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessStore {

    private static Map<String, UserInfo> emailAccounts = new HashMap<String, UserInfo>();
    private static ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();

    public ObservableList<String> getEmailAccountNames(){
        return emailAccountsNames;
    }

    public UserInfo getEmailAccountByName(String name){
        return emailAccounts.get(name);
    }

    public static void addAccount(UserInfo account){
        emailAccounts.put(account.getEmailAddress(), account);
        emailAccountsNames.add(account.getEmailAddress());
    }

    // The message that is selected and need to be passed to other fxml window.
    private MailStoreBeans clickedMessage;

    // one controller will set the message and other will receive the message.
    // So Each controller need reference to this class
    public MailStoreBeans getSelectedMessage() {
        return clickedMessage;
    }



    public void setSelectedMessage(MailStoreBeans msg) {
        this.clickedMessage = msg;
    }

    public EmailFolderBean getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailFolderBean selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private EmailFolderBean selectedFolder;

    private static List<Folder> foldersList = new ArrayList<Folder>();

    public List<Folder> getFoldersList(){
        return  foldersList;
    }

    public static void addFolder(Folder folder){
        foldersList.add(folder);
    }

}

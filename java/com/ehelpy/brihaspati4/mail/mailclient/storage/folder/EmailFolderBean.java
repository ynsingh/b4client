/*
When user clicks on tree items (folders), what to be done.
There are 2 type of tree items (email ID, folders)
 */
package com.ehelpy.brihaspati4.mail.mailclient.storage.folder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;
import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

// This class will do following
//01. hold number of read/unread message in a folder
//02.
public class EmailFolderBean<T, string> extends TreeItem<string> {
    private boolean topEmail  = false; //whether it is top root element of the tree
    private int unreadMessageCount; // total unread message in a folder
    private String dirName;    // name of folder
    @SuppressWarnings("unused")
    private String fullDirName;    // e.g INBOX.Folder1
    private ObservableList<MailStoreBeans> data = FXCollections.observableArrayList(); // observable is the mailstorage and each holds email messages

    /**
     * Constructor for top elements i.e sm@iitk.ac.in
     */

    public EmailFolderBean( String value){ // name is passed
        super((string) value, GuiMethods.defaultGui.iconImg(value)); //TreeItem class constructor called with  first name of folder, 2nd is the graphics(icon)
        this.dirName = value;
        this.fullDirName = value;
        data = null; // no data in the top element e.g. sm@iitk.ac.in does not have any data but a folder inbox has
        topEmail = true;
        this.setExpanded(true); // expand the tree items
    }
    /**
     * Constructor for standard folders e.g. INBOX
     * @param value
     */
    public EmailFolderBean(String value, String completeName){ //folder name is passed
        super((string) value, GuiMethods.defaultGui.iconImg(value));
        this.dirName = value;
        this.fullDirName = completeName;
    }
    // How many read/unread message in a folder
    private void updateUnreadMsgCount(){
        if(unreadMessageCount > 0){
            this.setValue((string) (dirName + "(" + unreadMessageCount + ")"));
        }else{
            this.setValue((string) dirName); // if there is no unread message just display the foldername
        }
    }

    //increment the count of unread messages for display only, once new messages arrive
    public void incrementUnreadMessagesCount(int newMessages){
        unreadMessageCount = unreadMessageCount + newMessages;
        updateUnreadMsgCount();
    }
    //decrement the count of unread messages for display only, once a message is read
    public void decrementUnreadMessagesCount(){
        unreadMessageCount--;
        updateUnreadMsgCount();
    }

    // This method will add mails to a folder and increment count
    public void addEmail(int position, Message message) throws MessagingException {
        boolean isRead = message.getFlags().contains(Flags.Flag.SEEN);
        MailStoreBeans emailMessageBean = new MailStoreBeans(message.getSubject(),
                message.getFrom()[0].toString(),
                message.getSentDate(),
                message.getSize(),"",isRead,message);
        if (position < 0) {
            data.add(emailMessageBean);
        }else{
            data.add(position, emailMessageBean); // add the new mail to top position(index 0)
        }
        if(!isRead){
            incrementUnreadMessagesCount(1);
        }

    }

    public boolean isTopElement(){
        return topEmail;
    }

    public ObservableList<MailStoreBeans> getData(){
        return data;
    }
}

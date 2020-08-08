package com.ehelpy.brihaspati4.mail.mailclient.logic;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Date;

import javafx.stage.Popup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import com.ehelpy.brihaspati4.mail.mailclient.logic.demons.EmailAccountControllerDemon;
import com.ehelpy.brihaspati4.mail.mailclient.logic.demons.PeriodicFolderMsgCountCheckDemon;
import com.ehelpy.brihaspati4.mail.mailclient.logic.demons.ShowEmailBodyDemon;
import com.ehelpy.brihaspati4.mail.mailclient.storage.ConfigData;
import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;
import com.ehelpy.brihaspati4.mail.mailclient.storage.folder.EmailFolderBean;
import com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable.EachMailProperty;
import com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable.MailSizeFormat;


public class ReadMainController extends AbstractController {
    final UserInfo emailAccountBean = new UserInfo();
    // Constructor for Abstract Controller
    public ReadMainController(AccessStore storAccess) {
        super(storAccess);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button compose;

    @FXML
    private TreeView<String> mailFolders;   //fxml id of tree view container

    private MenuItem viewHeader = new MenuItem("View Header");    // for right click menu on messageindex



    //We will use JavafX Table classes for Mail index related functionality
    @FXML
    private TableView<MailStoreBeans> mailIndex;

    @FXML
    private TableColumn<MailStoreBeans, String> subjC;

    @FXML
    private TableColumn<MailStoreBeans, String> fromC;

    @FXML
    private TableColumn<MailStoreBeans, Date> dateC;

    @FXML
    private TableColumn<MailStoreBeans, MailSizeFormat> sizeC;

    @FXML
    private WebView msgView;
    private ShowEmailBodyDemon showMailBody; //object which display the body in webview
    @FXML
    private MenuBar mailReadMenu;

    @FXML
    private Button replyMail;

    @FXML
    private Button deleteMail;

    @FXML
    private Button forwardMail;

    @FXML
    private TextField searchMail;

    @FXML
    private Text search;



    @FXML
    void readMainGoClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void readMainGoCompose(ActionEvent event) throws IOException {
        GuiMethods cgui = new GuiMethods();
        Scene cscene = cgui.getComposeScene();
        Stage cstage = new Stage();
        cstage.setScene(cscene);
        cstage.setTitle("Bmail Client: Compose your email");
        cstage.show();
    }

    @FXML
    void readMainGoSync(ActionEvent event) {
        Service<Void> emailService = new Service<Void>(){
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>(){
                    @Override
                    protected Void call() throws Exception {
                        ObservableList<MailStoreBeans> data = getStoreAccess().getSelectedFolder().getData();
                        //emailAccountBean.addEmailsToData(data);
                        return null;
                    }

                };
            }

        };
        emailService.start();
    }

    @FXML
    void readMainGoDelete(ActionEvent event) {

    }

    @FXML
    void readMainGoForward(ActionEvent event) {

    }

    @FXML
    void readMainGoHeader(ActionEvent event) {

    }

    @FXML
    void readMainGoAddressBook(ActionEvent event) {

    }
    @FXML
    void readMainGoAbout(ActionEvent event) {
        System.out.println("This is B4 Email Clinet");
    }

    @FXML
    void readUnread(ActionEvent event) {
       MailStoreBeans message = getStoreAccess().getSelectedMessage();
        if(message != null){
            boolean value = message.isRead();
            message.setRead(!value);
            EmailFolderBean selectedFolder = getStoreAccess().getSelectedFolder();
            if(selectedFolder != null){
                if(value){
                    selectedFolder.incrementUnreadMessagesCount(1);
                }else{
                    selectedFolder.decrementUnreadMessagesCount();
                }
            }
        }
    }

    @FXML
    void readMainGoReply(ActionEvent event) {

    }

    @FXML
    void readMainGoSearch(ActionEvent event) {

    }
    @FXML
    void readMainGotheme(ActionEvent event) {

    }



    @FXML
    void initialize() {
        assert compose != null : "fx:id=\"compose\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert mailFolders != null : "fx:id=\"mailFolders\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert mailIndex != null : "fx:id=\"mailIndex\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert subjC != null : "fx:id=\"subjC\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert fromC != null : "fx:id=\"fromC\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert dateC != null : "fx:id=\"dateC\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert sizeC != null : "fx:id=\"sizeC\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert msgView != null : "fx:id=\"msgView\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert mailReadMenu != null : "fx:id=\"mailReadMenu\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert replyMail != null : "fx:id=\"replyMail\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert deleteMail != null : "fx:id=\"deleteMail\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert forwardMail != null : "fx:id=\"forwardMail\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert searchMail != null : "fx:id=\"searchMail\" was not injected: check your FXML file 'ReadMain.fxml'.";
        assert search != null : "fx:id=\"search\" was not injected: check your FXML file 'ReadMain.fxml'.";

        showMailBody = new ShowEmailBodyDemon(msgView.getEngine());
        //Following 2 lines will check for new messages in each folder in 10 seconds.
        PeriodicFolderMsgCountCheckDemon folderCheck = new PeriodicFolderMsgCountCheckDemon(getStoreAccess().getFoldersList());
        folderCheck.start();

        //refer to the single storage object
        GuiMethods  storAccess = GuiMethods.defaultGui;
        //set cell values in mail index from propertyvaluefactory, uses MailStoreBeans
        subjC.setCellValueFactory(new PropertyValueFactory<MailStoreBeans, String>("sub"));
        fromC.setCellValueFactory(new PropertyValueFactory<MailStoreBeans, String>("from"));
        dateC.setCellValueFactory(new PropertyValueFactory<MailStoreBeans, Date>("date"));
        sizeC.setCellValueFactory(new PropertyValueFactory<MailStoreBeans, MailSizeFormat>("size"));
        mailIndex.setRowFactory(e-> new EachMailProperty<>());
        //BUG: size doesn't get it's default comparator overridden, so we do it by hand!!!!
        sizeC.setComparator(new MailSizeFormat(0));




/*
        //Populating the Email Folder Structure
        mailFolders.setRoot(root);

        GuiMethods icons = new GuiMethods();

        ConfigData ob1=ConfigData.INSTANCE;
        String email=ob1.show("Email");
        root.setValue(email);
        root.setGraphic(icons.iconImg(root.getValue()));

        //following for setting icons
        TreeItem<String> IMAP = new TreeItem<String>("IMAP", icons.iconImg("Inbox"));
        TreeItem<String> Inbox = new TreeItem<String>("Inbox", icons.iconImg("Inbox"));
        TreeItem<String> Sent = new TreeItem<String>("Sent", icons.iconImg("Sent"));
        TreeItem<String> Drafts = new TreeItem<String>("Drafts", icons.iconImg("Drafts"));
        TreeItem<String> Outbox = new TreeItem<String>("Outbox", icons.iconImg("Outbox"));
        TreeItem<String> Spam = new TreeItem<String>("Spam", icons.iconImg("Spam"));
        TreeItem<String> Trash = new TreeItem<String>("Trash", icons.iconImg("Trash"));
        TreeItem<String> Archive = new TreeItem<String>("Archive", icons.iconImg("Archive"));
        IMAP.getChildren().addAll(Inbox,Sent,Drafts,Outbox,Spam,Trash,Archive);
        TreeItem<String> P2P = new TreeItem<String>("B4", icons.iconImg("Inbox"));



        P2P.getChildren().addAll(Inbox, Sent, Drafts, Outbox, Spam, Trash,Archive);

        root.getChildren().addAll(IMAP,P2P);
        //root.getChildren().addAll(IMAP,Inbox, Sent, Drafts, Outbox, Spam, Trash);
        root.setExpanded(true);

*/
        EmailFolderBean root = new EmailFolderBean("");
        mailFolders.setRoot(root);
        mailFolders.setShowRoot(false);


        /*
        ConfigData ob1=ConfigData.INSTANCE;
        String email=ob1.show("Email");

        EmailFolderBean emailhead = new EmailFolderBean<>(email);
        root.getChildren().add(emailhead);
        EmailFolderBean Imap = new EmailFolderBean<>("IMAP");
        Imap.getChildren().addAll(Inbox,Sent,Drafts,Outbox,Spam,Trash,Archive);


        emailhead.getChildren().addAll(Imap,B4);

        */


        EmailAccountControllerDemon imapAccount= new EmailAccountControllerDemon(root,getStoreAccess());
        imapAccount.start();
       //EmailAccountControllerDemon b4Account= new EmailAccountControllerDemon( root,getStoreAccess());
        //b4Account.start();

        // When click on a folder, extract emails from that folders and display
        mailFolders.setOnMouseClicked(i -> {
            EmailFolderBean aFolder = (EmailFolderBean)mailFolders.getSelectionModel().getSelectedItem();
            if(aFolder != null && !aFolder.isTopElement()){
                mailIndex.setItems(aFolder.getData());
                getStoreAccess().setSelectedFolder(aFolder);
                //clear the selected message when going to another folders
                getStoreAccess().setSelectedMessage(null);
            }else{
               // System.out.println("Hi"); //TODO
                System.out.println("Running thread name is: " + Thread.currentThread().getName());
            }
        });

         //When click on a email in index view the content should be displayed by calling ShowEmailBody Demon
        mailIndex.setOnMouseClicked(i -> {
            MailStoreBeans aMail = mailIndex.getSelectionModel().getSelectedItem();
            if(aMail != null){
                getStoreAccess().setSelectedMessage(aMail);   // Access the message via Abstract controller and AccessStore controller
                showMailBody.setMessageToRender(aMail);
                showMailBody.restart();
            }
        });

        viewHeader.setOnAction(e->{
            Scene scene = storAccess.getEmailDetailsScene();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
        mailIndex.setContextMenu(new ContextMenu(viewHeader));
    }



}



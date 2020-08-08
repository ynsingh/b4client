package com.ehelpy.brihaspati4.mail.mailclient.logic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;

public class EmailWithHeaderController extends AbstractController {

    // Constructor for Abstract Controller
    public EmailWithHeaderController(AccessStore storAccess) {
        super(storAccess);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private WebView emailHeaderView;

    @FXML
    private Label subEmailHeader;

    @FXML
    private Label toEmailHeader;

    @FXML
    private Label fromEmailHeader;

    @FXML
    private Button save;

    @FXML
    private Button attachmentDownload;

    @FXML
    void downloadAttachment(ActionEvent event) {

    }

    @FXML
    void saveEmailtoDisk(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert emailHeaderView != null : "fx:id=\"emailHeaderView\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        assert subEmailHeader != null : "fx:id=\"subEmailHeader\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        assert toEmailHeader != null : "fx:id=\"toEmailHeader\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        assert fromEmailHeader != null : "fx:id=\"fromEmailHeader\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        assert attachmentDownload != null : "fx:id=\"attachmentDownload\" was not injected: check your FXML file 'EmailWithHeader.fxml'.";
        System.out.println("EmailWithHeaderController Initialized");


        MailStoreBeans selectedMessage = getStoreAccess().getSelectedMessage();


        subEmailHeader.setText("Subject: " + selectedMessage.getSub());
        fromEmailHeader.setText("Sender: " + selectedMessage.getFrom());

        //emailHeaderView.getEngine().loadContent(selectedMessage.getMailBody());

    }
}

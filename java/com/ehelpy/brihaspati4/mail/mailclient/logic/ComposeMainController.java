package com.ehelpy.brihaspati4.mail.mailclient.logic;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.scene.web.HTMLEditor;
import com.ehelpy.brihaspati4.mail.mailclient.logic.demons.ComposeB4Mail;
import com.ehelpy.brihaspati4.mail.mailclient.logic.demons.SendImapEmailDemon;
import com.ehelpy.brihaspati4.mail.mailclient.storage.MailStoreBeans;
import com.ehelpy.brihaspati4.mail.mailclient.storage.UserInfo;

import javax.mail.Store;


public class ComposeMainController extends AbstractController implements Initializable {

    // Constructor for Abstract Controller
    public ComposeMainController(AccessStore storAccess) {
        super(storAccess);
    }
    private List<File> attachments = new ArrayList<File>();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="attachmentsLable"
    private Label attachmentsLable; // Value injected by FXMLLoader

    @FXML // fx:id="senderChoice"
    private ChoiceBox<String>  senderChoice; // Value injected by FXMLLoader


    @FXML // fx:id="recipientField"
    private TextField recipientField; // Value injected by FXMLLoader

    @FXML // fx:id="subjectField"
    private TextField subjectField; // Value injected by FXMLLoader

    @FXML // fx:id="composeArea"
    private HTMLEditor composeArea; // Value injected by FXMLLoader

    @FXML // fx:id="errorLabel"
    private Label errorLabel; // Value injected by FXMLLoader

    @FXML // fx:id="sendMail"
    private Button sendMail; // Value injected by FXMLLoader

    @FXML
    void attchBtnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            attachments.add(selectedFile);
            attachmentsLable.setText(attachmentsLable.getText() + selectedFile.getName() + "; ");
        }
    }
    void composeMainGoClose(ActionEvent event) {
        System.exit(0);
    }


    @FXML
    void B4sendBtnAction(ActionEvent event) {
        errorLabel.setText("");
        ComposeB4Mail aB4Mail =
                new ComposeB4Mail(getStoreAccess().getEmailAccountByName(senderChoice.getValue()),
                        subjectField.getText(),
                        recipientField.getText(),
                        composeArea.getHtmlText(),
                        attachments);
        aB4Mail.restart();
    }


    @FXML
    void sendBtnAction(ActionEvent event) {
        errorLabel.setText("");
        SendImapEmailDemon sendmail =
                new SendImapEmailDemon(getStoreAccess().getEmailAccountByName(senderChoice.getValue()),
                        subjectField.getText(),
                        recipientField.getText(),
                        composeArea.getHtmlText(),
                        attachments);
        sendmail.restart();
        sendmail.setOnSucceeded(e->{
            if(sendmail.getValue() == UserInfo.MESSAGE_SENT_OK){
                errorLabel.setText("message sent successefully");
                composeClose();
            }else{
                errorLabel.setText("message sending error!!!");
            }
        });
    }

    private void composeClose(){
        // get a handle to the stage
        Stage stage = (Stage) sendMail.getScene().getWindow();
        // do what you have to do
        stage.close();
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL location, ResourceBundle resources) {
        assert attachmentsLable != null : "fx:id=\"attachmentsLable\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert senderChoice != null : "fx:id=\"senderChoice\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert recipientField != null : "fx:id=\"recipientField\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert subjectField != null : "fx:id=\"subjectField\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert composeArea != null : "fx:id=\"composeArea\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        assert sendMail != null : "fx:id=\"sendMail\" was not injected: check your FXML file 'ComposeMain.fxml'.";
        senderChoice.setItems(getStoreAccess().getEmailAccountNames());
        senderChoice.setValue(getStoreAccess().getEmailAccountNames().get(0));

    }
}

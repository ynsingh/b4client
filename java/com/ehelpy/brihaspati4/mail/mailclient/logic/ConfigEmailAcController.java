package com.ehelpy.brihaspati4.mail.mailclient.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;


public class ConfigEmailAcController extends AbstractController {

    // C
    // onstructor for Abstract Controller
    public ConfigEmailAcController(AccessStore storAccess) {
        super(storAccess);
    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuBar configMenu;

    @FXML
    private TextField emailId;

    @FXML
    private TextField password;

    @FXML
    private TextField imaphost;

    @FXML
    private TextField imapport;

    @FXML
    private TextField smtphost;

    @FXML
    private TextField smtpport;

    @FXML
    private Button createConfigNxt;

    @FXML
    private Button createConfigClose;

    @FXML
    void createConfigGoNxt(ActionEvent event) throws IOException{
        System.out.println("createConfig Button clicked");
        //uncomment following in production, to check whether fileds are entred or not.
//        Window owner = createConfigNxt.getScene().getWindow();
//        if(emailId.getText().isEmpty()) {
//            UtilMethods.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                    "Please enter your Email");
//            return;
//        }
//        if(password.getText().isEmpty()) {
//            UtilMethods.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                    "Please enter your email password");
//            return;
//        }
//        if(imaphost.getText().isEmpty()) {
//            UtilMethods.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                    "Please enter a IMAP Server");
//            return;
//        }
//
//        if(smtphost.getText().isEmpty()) {
//            UtilMethods.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                    "Please enter a SMTP Server");
//            return;
//        }
//
//        UtilMethods.showAlert(Alert.AlertType.CONFIRMATION, owner, "Fields saved Successfully!",
//                "Welcome " + emailId.getText());

        // TODO: encrypt the content of this file with master password
        StringBuilder str=new StringBuilder();
        str.append("Email="+ emailId.getText() +"\n");
        //Make SHA-512 and base64 enclding to store password
        String upass= password.getText();
        str.append("Password="+upass+"\n");//TODO : Check password is matching
        str.append("IMAPHOST="+ imaphost.getText() +"\n");
        str.append("SMTPHOST="+ smtphost.getText() +"\n");
        str.append("IMAPORT="+ imapport.getText() +"\n");
        str.append("SMTPPORT="+ smtpport.getText() +"\n");
        System.out.println(str);
        //TODO
        //check if config input is null, then reask user to input data.
        //if(imaphost.getText().isEmpty())
        //String encStr=UtilMethods.passEncMethod(str.toString());
        File configFile=new File("Config.properties");
        FileWriter W;
        if (configFile.exists())
        {
            W = new FileWriter(configFile,true);//if file exists append to file. Works fine.
        }
        else
        {
            configFile.createNewFile();
            W = new FileWriter(configFile);
        }
        //=new FileWriter(configFile);
        //W.write(str.toString());
        W.write(str.toString());
        W.close();


        //TODO : Check SMTP and IMAP server rechability with user password
        Stage stage = (Stage) createConfigNxt.getScene().getWindow();
        stage.close();

        try {
            GuiMethods gui = new GuiMethods();
            Scene scene = gui.getmasterPassScene();
            Stage readmailstage = new Stage();
            readmailstage.setScene(scene);
            readmailstage.setTitle("Bmail Client: Set a master password");
            //st2.setResizable(false);
            //st2.setFullScreen(true);
            //readmailstage.setMaximized(true);
            readmailstage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void importConfig(ActionEvent event) {
        System.out.println("Import Config Button clicked");
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            emailId.setText(prop.getProperty("Email"));
            imaphost.setText(prop.getProperty("IMAPHOST"));
            imapport.setText(prop.getProperty("IMAPORT"));
            smtphost.setText(prop.getProperty("SMTPHOST"));
            smtpport.setText(prop.getProperty("SMTPPORT"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }



    }


    @FXML
    void exportConfig(ActionEvent event) {
        System.out.println("Export Config Button clicked");
    }


    @FXML
    void showHelp(ActionEvent event) {
        System.out.println("User asking for help");
    }


    @FXML
    void createConfigGoClose(ActionEvent event) {
        System.out.println("Button clicked");
        // Close after confirmation

        Boolean ans=ConfirmBox.display("Title",  "Are you sure to exit?");
        if(ans) {
            Stage stage = (Stage) createConfigClose.getScene().getWindow();
            stage.close();
        }
        //Platform.exit();
        //System.exit(0);
    }

    @FXML
    void initialize() {
        assert configMenu != null : "fx:id=\"configMenu\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert emailId != null : "fx:id=\"emailId\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert imaphost != null : "fx:id=\"imaphost\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert imapport != null : "fx:id=\"imapport\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert smtphost != null : "fx:id=\"smtphost\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert smtpport != null : "fx:id=\"smtpport\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";
        assert createConfigNxt != null : "fx:id=\"createConfigNxt\" was not injected: check your FXML file 'ConfigEmailAc.fxml'.";

    }



}

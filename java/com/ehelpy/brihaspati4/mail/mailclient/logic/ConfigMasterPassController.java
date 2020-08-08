package com.ehelpy.brihaspati4.mail.mailclient.logic;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;

public class ConfigMasterPassController extends AbstractController{

    // Constructor for Abstract Controller
    public ConfigMasterPassController(AccessStore storAccess) {
        super(storAccess);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passMasterCfg;

    @FXML
    private TextField emailMasterCfg;

    @FXML
    private Button delMasterCfg;

    @FXML
    private PasswordField repassMasterCfg;

    @FXML
    private Button chgMasterCfg;

    @FXML
    private Button saveMasterCfg;

    @FXML
    void chgMasterCfgGo(ActionEvent event) {

    }

    @FXML
    void delMasterCfgGo(ActionEvent event) {

    }

    @FXML
    void saveMasterCfgGo(ActionEvent event) throws IOException{
        System.out.println("create master password Button clicked");

        String masterPass= passMasterCfg.getText();
        String masterRePass=repassMasterCfg.getText();
        if (!masterPass.equals(masterRePass)){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning alert");
            alert.setHeaderText("Passwords does not match");
            alert.setContentText("Please reenter the  master passwords again!");
            alert.showAndWait();
            if (alert.isShowing()) {
                Platform.runLater(() -> alert.close());
                }
        }else {

            String encMasterPass=UtilMethods.passEncMethod(masterPass.toString());
            File masterPassFile=new File(".secret");
            FileWriter F=new FileWriter(masterPassFile);
            F.write(encMasterPass);
            F.close();

            Stage stage = (Stage) saveMasterCfg.getScene().getWindow();
            stage.close();

            try {
                GuiMethods gui = new GuiMethods();
                Scene scene = gui.getReadMainScene();
                Stage readmailstage = new Stage();
                readmailstage.setScene(scene);
                readmailstage.setTitle("B4mail Client: Welcome");
                //st2.setResizable(false);
                //st2.setFullScreen(true);
                readmailstage.setMaximized(true);
                readmailstage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void initialize() {
        assert passMasterCfg != null : "fx:id=\"passMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";
        assert emailMasterCfg != null : "fx:id=\"emailMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";
        assert delMasterCfg != null : "fx:id=\"delMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";
        assert repassMasterCfg != null : "fx:id=\"repassMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";
        assert chgMasterCfg != null : "fx:id=\"chgMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";
        assert saveMasterCfg != null : "fx:id=\"saveMasterCfg\" was not injected: check your FXML file 'ConfigMaster.fxml'.";

        try (InputStream input = new FileInputStream("Config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            emailMasterCfg.setText(prop.getProperty("Email"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

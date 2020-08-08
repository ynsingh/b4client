package com.ehelpy.brihaspati4.mail.mailclient.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.stage.Stage;
import com.ehelpy.brihaspati4.mail.mailclient.logic.*;

import java.io.IOException;
import java.util.ArrayList;

// This class is the interface to other classes

public  class GuiMethods {

    private final String MAIN_CSS = "css/themePurple.css";
    private final String FIRST_FXML = "Main.fxml";
    private final String ADD_ACCOUNT_FXML = "ConfigEmailAc.fxml";
    private final String ADD_MASTERPASS_FXML = "ConfigMaster.fxml";
    private final String HEADER_FXML = "EmailWithHeader.fxml";
    private final String INDEX_SCREEN_FXML = "ReadMain.fxml";
    private final String COMPOSE_SCREEN_FXML = "ComposeMain.fxml";
    private final String THEME_SCREEN_FXML = "ThemeFont.fxml";
    //Single Static object
    public static GuiMethods defaultGui = new GuiMethods();

    // Access to store via this object.
    private AccessStore storAccess = new AccessStore();

    // Reference to All controllers(lazy instantiaation)
    private MainController mainController;
    private ConfigEmailAcController addAccountController;
    private ConfigMasterPassController masterPassController;

    private ReadMainController mainReadController;
    private EmailWithHeaderController emailHeaderController;
    private ComposeMainController emailComposeController;
    private ThemeController themeController;

    public Scene getFirstScene(){
        mainController = new MainController(storAccess);
        return initializeScene(FIRST_FXML, mainController);
    }

    public Scene getAddAccountScene(){
        addAccountController = new ConfigEmailAcController(storAccess);
        return initializeScene(ADD_ACCOUNT_FXML, addAccountController);
    }

    public Scene getmasterPassScene(){
        masterPassController = new ConfigMasterPassController(storAccess);
        return initializeScene(ADD_MASTERPASS_FXML, masterPassController);
    }

    public Scene getReadMainScene(){
        mainReadController = new ReadMainController(storAccess);
        return initializeScene(INDEX_SCREEN_FXML, mainReadController);
    }

    public Scene getEmailDetailsScene(){
        emailHeaderController = new EmailWithHeaderController(storAccess);
        return initializeScene(HEADER_FXML, emailHeaderController);
    }

    public Scene getComposeScene(){
        emailComposeController = new ComposeMainController(storAccess);
        return initializeScene(COMPOSE_SCREEN_FXML, emailComposeController);
    }
    public Scene showThemeWindow(){
        themeController = new ThemeController(storAccess);
        return initializeScene(THEME_SCREEN_FXML, themeController);

    }
    // This method fixes image for each folder
    public  Node iconImg(String treeItemValue){
        String folderlow = treeItemValue.toLowerCase();
        ImageView icons;
        try {
            if(folderlow.contains("inbox")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Inbox.png")));
            }
            else if(folderlow.contains("drafts")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Drafts.png")));
            }
            else if(folderlow.contains("outbox")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Outbox.png")));
            }
            else if(folderlow.contains("sent")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Sent.png")));
            }
            else if(folderlow.contains("spam")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Spam.png")));
            }
            else if(folderlow.contains("trash")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/Trash.png")));
            }
            else if(folderlow.contains("@")){
                icons= new ImageView(new Image(getClass().getResourceAsStream("images/email.png")));
            }
            else {
                icons = new ImageView(new Image(getClass().getResourceAsStream("images/Default.png")));
            }
        }
        catch (NullPointerException e) {
            System.out.println("Check Icon Image !!!");
            e.printStackTrace();
            icons = new ImageView();
        }

        icons.setFitHeight(16);
        icons.setFitWidth(16);

        return icons;
    }

    // Following methods will help in initializing scenes by FXML files
    // First argument is the fxml file path, second is the controller which has access to Storage access.
    // Steps: 1) FXMLLoader creates an instance of the corresponding controller
    private Scene initializeScene(String pathToFxml, AbstractController aController){
        FXMLLoader loader;
        Parent parent;
        Scene scene;

        loader = new FXMLLoader(getClass().getResource(pathToFxml));//get the FXML to a scene
        loader.setController(aController); //attach to the corresponding controller
        try {
            parent = loader.load(); //load the fxml file from disk
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource(MAIN_CSS).toExternalForm());
        return scene;
    }


}

/* based on work of Alex Horea, original code taken from github in May 2018 */

 package com.ehelpy.brihaspati4.mail.mailclient;

import com.ehelpy.brihaspati4.ReplicaMgt.Monitor;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/*
This is the start of mail client.
1. Any JavaFX class extends the JavaFX application abstract class.
2. The start method of the class is to be implemented to make, Apps, which takes an stage as argument.
3. The main method will lunch the app
4.
 */

public class MailCli extends Application {

    //public static void main(String[] args) {
     //   launch(args);
    //}


    //start() method is the starting point of constructing a JavaFX application
    // override start method of javafx.application.Application class to use this app.
    // Object of the class javafx.stage.Stage is passed into the start() method
    @Override
    public void start(Stage primaryStage) throws Exception {

        GuiMethods gui = GuiMethods.defaultGui;

        File path = new File("Config.properties");
        if (path.exists() && path.canRead()) {
            Scene scene = gui.getReadMainScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bmail Client");
            primaryStage.show();
        }else{
            Scene scene = gui.getFirstScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bmail Client: Add account");
            primaryStage.show();
        }

    }
}




package com.ehelpy.brihaspati4.mail.mailclient.logic;

import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileDialog extends Application {

    private Text actionStatus;
    private Stage savedStage;
    private static final String titleTxt = "JavaFX File Chooser Example 1";

    @Override
    public void start(Stage stage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null){
            System.out.println("File selected: " + selectedFile.getName());
        }
        else{
            System.out.println("Please select a file");
        }
    }

}

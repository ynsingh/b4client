package com.ehelpy.brihaspati4.mail.mailclient.logic;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;
import javafx.stage.Stage;
// controller can define an initialize() method, which will be called once on an implementing controller when the contents of its associated document have been completely loaded.
// constructor is called first, then any @FXML annotated fields are populated then initialize() is called.
// constructor does NOT have access to @FXML fields referring to components defined in the .fxml file, but initialize() does have access to them.
// with Initializable we have more control over the behavior of the controller and the elements it manages.
public class MainController extends AbstractController implements Initializable {


    // Constructor which call Abstract Controller constructor
    public MainController(AccessStore storAccess) {
        super(storAccess);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink addAc;

    @FXML
    private Hyperlink openAc;

    @FXML
    void AddAccount(ActionEvent event) {

        Stage stage = (Stage) addAc.getScene().getWindow();
        stage.close();

        System.out.println("Button Add account clicked");
        GuiMethods gui = new GuiMethods();
        Scene scene = gui.getAddAccountScene();
        Stage addacstage = new Stage();
        addacstage.setScene(scene);
        addacstage.setTitle("Bmail Client: Add  Email account");
        addacstage.show();


    }

    @FXML
    void OpenAccount(ActionEvent event) {
        System.out.println("Button clicked");
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        assert addAc != null : "fx:id=\"addAc\" was not injected: check your FXML file 'Main.fxml'.";
        assert openAc != null : "fx:id=\"openAc\" was not injected: check your FXML file 'Main.fxml'.";


    }





}


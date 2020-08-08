package com.ehelpy.brihaspati4.mail.mailclient.logic;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import com.ehelpy.brihaspati4.mail.mailclient.gui.GuiMethods;

public class ThemeController extends AbstractController{
    public ThemeController(AccessStore storAccess) {
        super(storAccess);
    }
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fontSizePicker"
    private Slider fontSizePicker; // Value injected by FXMLLoader

    @FXML // fx:id="themePicker"
    private ChoiceBox<?> themePicker; // Value injected by FXMLLoader

    @FXML
    void applyBtnAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert fontSizePicker != null : "fx:id=\"fontSizePicker\" was not injected: check your FXML file 'ThemeFont.fxml'.";
        assert themePicker != null : "fx:id=\"themePicker\" was not injected: check your FXML file 'ThemeFont.fxml'.";

    }
}

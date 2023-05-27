package controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;

public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nickText;
    @FXML
    private Button volverButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
    }

}

package controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafxmlapplication.GreenBallApp;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot("inicio");
    }

}

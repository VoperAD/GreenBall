package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;

public class UserController implements Initializable {

    @FXML
    private Button reservarButton;
    @FXML
    private Button misReservasButton;
    @FXML
    private Button configButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.CONFIG_PERFIL));
    }

    @FXML
    private void cerrarSesionAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
    }
}

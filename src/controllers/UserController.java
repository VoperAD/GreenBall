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
    private Button salirButton;
    @FXML
    private Button reservarButton;
    @FXML
    private Button misReservasButton;
    @FXML
    private Button configButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configButton.setOnAction(event -> {
            GreenBallApp.reloadScene(Scenes.CONFIG_PERFIL);
            GreenBallApp.setRoot(Scenes.CONFIG_PERFIL);});
        misReservasButton.setOnAction(event -> {
            GreenBallApp.reloadScene(Scenes.MIS_RESERVAS);
            GreenBallApp.setRoot(Scenes.MIS_RESERVAS);
        });
        reservarButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.HORARIOS_CON_SESION));
        salirButton.setOnAction(this::onSalir);
    }

    private void onSalir(ActionEvent event) {
        GreenBallApp.setUser(null);
        GreenBallApp.setRoot(Scenes.INICIO);
    }

}

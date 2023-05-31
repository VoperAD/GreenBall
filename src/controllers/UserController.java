package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
        reservarButton.setOnAction(event -> {
            GreenBallApp.reloadScene(Scenes.HORARIOS_CON_SESION);
            GreenBallApp.setRoot(Scenes.HORARIOS_CON_SESION);
        });
        salirButton.setOnAction(this::onSalir);
    }

    private void onSalir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("¿Seguro que deseas salir?");
        alert.setContentText("Se cerrará sesión");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/estilos/global-style.css").toExternalForm());
        //Asigna la clase .myAlert al contenedor principal del diálogo
        alert.getDialogPane().getStyleClass().add("info");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            GreenBallApp.setUser(null);
            GreenBallApp.setRoot(Scenes.INICIO);
        }
        
    }

}

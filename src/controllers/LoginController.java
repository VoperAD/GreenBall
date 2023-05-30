package controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Club;
import model.Member;
import utils.AlertUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nickText;
    @FXML
    private Button volverButton;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.disableProperty().bind(Bindings.or(passwordField.textProperty().isEmpty(), nickText.textProperty().isEmpty()));
    }

    @FXML
    private void onLogin(ActionEvent event) {
        Club club = GreenBallApp.getClub();
        String nickname = nickText.getText();
        String password = passwordField.getText();

        // Nickname inexistente
        if (!club.existsLogin(nickname)) {
            Alert alert = AlertUtils.createAlert(Alert.AlertType.ERROR, "Error en el Login!", "El usuario introducido no existe!");
            alert.showAndWait();
            return;
        }

        Member memberByCredentials = club.getMemberByCredentials(nickname, password);

        // Contraseña errónea
        if (memberByCredentials == null) {
            Alert alert = AlertUtils.createAlert(Alert.AlertType.ERROR, "Error en el Login!", "La contraseña introducida es incorrecta!");
            alert.showAndWait();
            return;
        }

        passwordField.clear();
        nickText.clear();

        AlertUtils.createAlert(Alert.AlertType.INFORMATION, "Sesión iniciada con éxito!", "");
        GreenBallApp.setUser(memberByCredentials);

        GreenBallApp.reloadScene(Scenes.MIS_RESERVAS);
        GreenBallApp.reloadScene(Scenes.CONFIG_PERFIL);
        GreenBallApp.reloadScene(Scenes.HORARIOS_CON_SESION);

        GreenBallApp.setRoot(Scenes.USER);
    }

    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
        GreenBallApp.reloadScene(Scenes.LOGIN);
    }

}

package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Member;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigPerfilController implements Initializable {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private TextField telField;
    @FXML
    private TextField nickField;
    @FXML
    private TextField tarjetaField;
    @FXML
    private TextField cvvField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button guardarCambiosButton;
    @FXML
    private Button volverButton;
    @FXML
    private Button restoreButton;
    @FXML
    private ImageView imagen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volverButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.USER));
        restoreButton.setOnAction(event -> this.setFields());

        this.setFields();
        nickField.setDisable(true);
    }

    @FXML
    private void onGuardarCambios(ActionEvent event) {
        Member user = GreenBallApp.getUser();

        // TODO averiguar todas las condiciones antes de guardar los cambios


        user.setImage(imagen.getImage());
        user.setName(nombreField.getText());
        user.setSurname(apellidosField.getText());
        user.setTelephone(telField.getText());
        user.setPassword(passwordField.getText());
        user.setCreditCard(tarjetaField.getText());
        user.setSvc(Integer.parseInt(cvvField.getText()));
    }

    private void setFields() {
        Member user = GreenBallApp.getUser();
        if (user == null) {
            throw new IllegalStateException("No se ha establecido el user global correctamente");
        }
        imagen.setImage(user.getImage());
        nombreField.setText(user.getName());
        apellidosField.setText(user.getSurname());
        telField.setText(user.getTelephone());
        nickField.setText(user.getNickName());
        passwordField.setText(user.getPassword());
        cvvField.setText(String.valueOf(user.getSvc()));
    }

}

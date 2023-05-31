package controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Member;
import utils.AlertUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class ConfigPerfilController implements Initializable {

//    @FXML
//    private Button seePasswordButton;
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
    private Button cambiarImagenButton;
    @FXML
    private Circle circleImage;

    private Image imagen;
    private SimpleBooleanProperty fieldsModified = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volverButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.USER));
        restoreButton.setOnAction(event -> this.setFields());
//        seePasswordButton.setOnMouseClicked(mouseEvent -> {});

        // Si no hay cambios en ningún field, deshabilitar el botón
        guardarCambiosButton.disableProperty().bind(fieldsModified.not());

        // Conjunto de listeners para reconocer cambios en el formulario
        Member user = GreenBallApp.getUser();
        BiConsumer<String, String> catchChanges = (newValue, field) -> { if (!newValue.equals(field)) fieldsModified.set(true); };
        nombreField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, user.getName()));
        apellidosField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, user.getSurname()));
        telField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, user.getTelephone()));
        tarjetaField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, user.getCreditCard()));
        cvvField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, String.valueOf(user.getSvc())));
        passwordField.textProperty().addListener((obs, old, newValue) -> catchChanges.accept(newValue, user.getPassword()));

        this.setFields();
        nickField.setDisable(true);
        
    }

    @FXML
    private void onGuardarCambios(ActionEvent event) {
        Member user = GreenBallApp.getUser();

        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("GreenBall informa");

        String nombreFieldText = nombreField.getText();
        if (!nombreFieldText.matches("^[a-zA-Z ]+$") || nombreFieldText.isBlank()) {
            error.setHeaderText("Valor inválido para el campo \"Nombre\"");
            error.setContentText("El campo no puede estar vacío y solo se aceptan letras.");
            error.showAndWait();
            return;
        }

        String apellidosFieldText = apellidosField.getText();
        if (!apellidosFieldText.matches("^[a-zA-Z ]+$") || apellidosFieldText.isBlank()) {
            error.setHeaderText("Valor inválido para el campo \"Apellidos\"");
            error.setContentText("El campo no puede estar vacío y solo se aceptan letras.");
            error.showAndWait();
            return;
        }

        String telFieldText = telField.getText();
        if (!telFieldText.matches("[0-9]*") || telFieldText.length() != 9) {
            error.setHeaderText("Valor inválido para el campo \"Teléfono\"");
            error.setContentText("Solo se aceptan números y el campo debe tener una longitud de 9 números.");
            error.showAndWait();
            return;
        }

        String passwordText = passwordField.getText();
        if (!passwordText.matches("[a-zA-Z0-9]*") || passwordText.length() < 7) {
            error.setHeaderText("Valor inválido para el campo \"Contraseña\"");
            error.setContentText("El campo debe tener una longitud mínima igual a 7 y solo se aceptan números y letras.");
            error.showAndWait();
            return;
        }

        String tarjeta = tarjetaField.getText();
        String cvv = cvvField.getText();
        if ((tarjeta.isEmpty() && !cvv.isEmpty()) || (!tarjeta.isEmpty() && cvv.isEmpty())) {
            error.setHeaderText("Valor inválido para los datos de la tarjeta.");
            error.setContentText("Si se introduce un número de tarjeta el campo del código de seguridad no puede estar vacío y viceversa.");
            error.showAndWait();
            return;
        }

        if (!tarjeta.isEmpty()) {
            if (!tarjeta.matches("[0-9]*") || tarjeta.length() != 16) {
                error.setHeaderText("Valor inválido para el campo \"Número de tarjeta\"");
                error.setContentText("Solo se aceptan números y el campo debe tener una longitud igual a 16.");
                error.showAndWait();
                return;
            }

            if (!cvv.matches("[0-9]*") || cvv.length() != 3) {
                error.setHeaderText("Valor inválido para el campo \"Código de seguridad\"");
                error.setContentText("Solo se aceptan números y el campo debe tener una longitud igual a 3.");
                error.showAndWait();
                return;
            }
        }

        user.setImage(imagen);
        user.setName(nombreFieldText);
        user.setSurname(apellidosFieldText);
        user.setTelephone(telFieldText);
        user.setPassword(passwordText);
        user.setCreditCard(tarjeta);
        user.setSvc(cvv.isBlank() ? 0 : Integer.parseInt(cvvField.getText()));
        
        Alert success = AlertUtils.createAlert(Alert.AlertType.INFORMATION, "Los cambios se han guardado correctamente");
        success.showAndWait();

        fieldsModified.set(false);

    }

    private void setFields() {
        Member user = GreenBallApp.getUser();
        if (user == null) {
            throw new IllegalStateException("No se ha establecido el user global correctamente");
        }
        imagen = user.getImage();
        circleImage.setFill(new ImagePattern(imagen));
        nombreField.setText(user.getName());
        apellidosField.setText(user.getSurname());
        telField.setText(user.getTelephone());
        nickField.setText(user.getNickName());
        tarjetaField.setText(user.getCreditCard());
        passwordField.setText(user.getPassword());
        cvvField.setText(user.getSvc() != 0 ? String.valueOf(user.getSvc()) : "");
    }

    @FXML
    private void cambiarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                fieldsModified.set(true);
                imagen = new Image(selectedFile.toURI().toURL().toExternalForm(),150,150,false,true);
                circleImage.setFill(new ImagePattern(imagen));
            } catch (MalformedURLException ex) {
                AlertUtils.createAlert(Alert.AlertType.ERROR, "Error al cargar la imagen.");
            }
            
        }
    }

}

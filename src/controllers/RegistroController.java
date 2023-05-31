/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Club;
import model.ClubDAOException;
import model.Member;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class RegistroController implements Initializable {

    @FXML
    private Button volverButtton;
    @FXML
    private TextField nombreText;
    @FXML
    private TextField apellidosText;
    @FXML
    private TextField telText;
    @FXML
    private TextField nickText;
    @FXML
    private TextField tarjetaText;
    @FXML
    private TextField ccvText;
    @FXML
    private Button registroButton;
    @FXML
    private PasswordField contText;
    @FXML
    private Label ccvErrLabel;
    @FXML
    private Label tarjetaErrLabel;
    @FXML
    private Label contErrLabel;
    @FXML
    private Label nickErrLabel;
    @FXML
    private Label apellidosErrLabel;
    @FXML
    private Label nombreErrLabel;
    @FXML
    private Label telErrLabel;
    @FXML
    private CheckBox visibleCheck;
    @FXML
    private TextField contVisibleText;

    private final SimpleBooleanProperty nombreProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty apellidosProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty telefonoProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty nicknameProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty passwordProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty tarjetaProperty = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty cvvProperty = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty okProperty = new SimpleBooleanProperty(true);

    private Club club = GreenBallApp.getClub();
    private Image imageOfCircle;
    @FXML
    private Button cambiarImagenButton;
    @FXML
    private Circle circleImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        imageOfCircle = new Image("/images/userIcon.png",150,150,false,true);
        circleImage.setFill(new ImagePattern(imageOfCircle));
        registroButton.disableProperty().bind(Bindings.not(Bindings.createBooleanBinding(() ->
                    nombreProperty.get() && apellidosProperty.get() && telefonoProperty.get() && nicknameProperty.get() &&
                            passwordProperty.get() && okProperty.get(),
                nombreProperty, apellidosProperty, telefonoProperty, nicknameProperty, passwordProperty, okProperty
        )));
        
        List<Member> miembros = club.getMembers();
        List<String> nicknames = new ArrayList<>();
        miembros.forEach(member -> nicknames.add(member.getNickName()));

        nombreText.textProperty().addListener((a,b,c) -> {
            if(nombreText.getText().length()>0){
                nombreText.setText(String.valueOf(nombreText.getText().charAt(0)).toUpperCase()+nombreText.getText().substring(1));
            }

            boolean ok = true;
            if(!nombreText.getText().matches("[a-zA-Z]*")){
                nombreErrLabel.setText("Rango de carácteres permitido: a-Z");
                ok = false;
            }
            if(ok|| nombreText.getText().isEmpty()){
                nombreText.setStyle("-fx-text-fill: black");
                nombreErrLabel.setText("");
            } else{
                nombreText.setStyle("-fx-text-fill: red");
            }

            nombreProperty.set(ok);
        });
        
        apellidosText.textProperty().addListener((a,b,c) -> {
            if(apellidosText.getText().length()>0){
                apellidosText.setText(String.valueOf(apellidosText.getText().charAt(0)).toUpperCase()+apellidosText.getText().substring(1));
            }

            boolean ok = true;
            if(!apellidosText.getText().matches("[a-zA-Z]*") && !apellidosText.getText().matches("[a-zA-Z]*\\s.[a-zA-Z]*")){
                apellidosErrLabel.setText("Rango de carácteres permitido: a-Z");
                ok = false;
            }
            if(ok|| apellidosText.getText().isEmpty()){
                apellidosText.setStyle("-fx-text-fill: black");
                apellidosErrLabel.setText("");
            } else{
                apellidosText.setStyle("-fx-text-fill: red");
            }

            apellidosProperty.set(ok);
        });
        
        telText.textProperty().addListener((a,b,c) -> {

            boolean ok = true;
            if(!telText.getText().matches("[0-9]*")){
                telErrLabel.setText("Rango de carácteres permitido: 0-9");
                ok = false;
            }
            if(telText.getText().length()!=9){
                telErrLabel.setText("El número debe contener 9 digitos");
                ok = false;
            }
            if(ok|| telText.getText().length()==0){
                telText.setStyle("-fx-text-fill: black");
                telErrLabel.setText("");
            } else{
                telText.setStyle("-fx-text-fill: red");
            }

            telefonoProperty.set(ok);
        });
        
        nickText.textProperty().addListener((a,b,c) -> {

            boolean ok = true;
            if(!nickText.getText().matches("[a-zA-Z0-9]*")){
                nickErrLabel.setText("Rango de carácteres permitido: alfanumérico");
                ok = false;
            }
            if(nicknames.contains(nickText.getText())){
                nickErrLabel.setText("Nick no disponible");
                ok = false;
            }

            if (ok|| nickText.getText().isEmpty()) {
                nickText.setStyle("-fx-text-fill: black");
                nickErrLabel.setText("");
            } else {
                nickText.setStyle("-fx-text-fill: red");
            }

            nicknameProperty.set(ok);
        });
        
        contText.textProperty().addListener((a,b,c) -> {

            boolean ok = true;
            if(!contText.getText().matches("[a-zA-Z0-9]*")){
                contErrLabel.setText("Rango de carácteres permitido: alfanumérico");
                ok = false;
            }
            if(contText.getText().length()<7){
                contErrLabel.setText("Longitud insuficiente");
                ok = false;
            }
            if(ok|| contText.getText().length()==0){
                contText.setStyle("-fx-text-fill: black");
                contVisibleText.setStyle("-fx-text-fill: black");
                contErrLabel.setText("");
            } else{
                contText.setStyle("-fx-text-fill: red");
                contVisibleText.setStyle("-fx-text-fill: red");
            }

            passwordProperty.set(ok);
        });
        
        tarjetaText.textProperty().addListener((a,b,c) -> {
            boolean ok = true;
            String text = tarjetaText.getText();

            if(text.length()!=16){
                tarjetaErrLabel.setText("El número debe contener 16 digitos");
                ok = false;
            }

            if(!text.matches("[0-9]*")){
                tarjetaErrLabel.setText("Rango de carácteres permitido: 0-9");
                ok = false;
            }

            if (ok) {
                tarjetaText.setStyle("-fx-text-fill: black");
                tarjetaErrLabel.setText("");
                ccvText.setDisable(false);
                tarjetaProperty.set(true);
            } else if(text.isEmpty()){
                tarjetaText.setStyle("-fx-text-fill: black");
                tarjetaErrLabel.setText("");
                okProperty.set(true);
                tarjetaProperty.set(false);
                ccvText.setDisable(true);
                cvvProperty.set(false);
                ccvText.setText("");
                ccvText.setStyle("-fx-text-fill: black");
                ccvErrLabel.setText("");
            }else{
                tarjetaText.setStyle("-fx-text-fill: red");
                ccvText.setDisable(true);
                cvvProperty.set(false);
                tarjetaProperty.set(false);
                okProperty.set(false);
                ccvText.setText("");
                ccvText.setStyle("-fx-text-fill: black");
                ccvErrLabel.setText("");
            }
        });
        
        ccvText.textProperty().addListener((a,b,c) -> {
            boolean ok = true;

            if (ccvText.getText().length()!=3) {
                ccvErrLabel.setText("El código debe tener 3 números");
                ok = false;
            }
            if(!ccvText.getText().matches("[0-9]*" )){
                ccvErrLabel.setText("El código debe ser numérico");
                ok = false;
            }
            if(ok && tarjetaProperty.get()){
                ccvText.setStyle("-fx-text-fill: black");
                ccvErrLabel.setText("");
                cvvProperty.set(true);
                okProperty.set(true);
            }else if (ok){
                ccvText.setStyle("-fx-text-fill: black");
                ccvErrLabel.setText("");
                cvvProperty.set(true);
                okProperty.set(false);
            } else if (ccvText.getText().isEmpty() && tarjetaText.getText().isEmpty()){
                okProperty.set(true);
                cvvProperty.set(false);
            }else{
                ccvText.setStyle("-fx-text-fill: red");
                okProperty.set(false);
                cvvProperty.set(false);
            }
        });

        ccvText.setDisable(true);

        contVisibleText.textProperty().bindBidirectional(contText.textProperty());
        contText.disableProperty().bind(visibleCheck.selectedProperty());
        contText.visibleProperty().bind(Bindings.not(visibleCheck.selectedProperty()));
        contVisibleText.disableProperty().bind(Bindings.not(visibleCheck.selectedProperty()));
        contVisibleText.visibleProperty().bind(visibleCheck.selectedProperty());
        
    }

    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
        vaciarCampos();
    }

    @FXML
    private void registrarAction(ActionEvent event) {
        try{
            club = Club.getInstance();
            
            String name, surname,telephon, login, password, creditCard, imagepath;
            int svc;
                
            name = nombreText.getText();
            surname = apellidosText.getText();
            telephon = telText.getText();
            login = nickText.getText();
            password = contText.getText();
            creditCard = tarjetaText.getText();
            if(!ccvText.getText().isEmpty()){
                svc = Integer.parseInt(ccvText.getText());
            }else{
                svc = 0;
            }
            
            
            Member result = club.registerMember(name, surname, telephon, login, password, creditCard, svc, imageOfCircle);
            
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("GreenBall Informa");
            dialog.setHeaderText("Usuario creado correctamente");
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
            getClass().getResource("/estilos/global-style.css").toExternalForm());
            //Asigna la clase .myAlert al contenedor principal del diálogo
            dialog.getDialogPane().getStyleClass().add("info");
            dialog.showAndWait();
            
            vaciarCampos();
            
        } catch (ClubDAOException | IOException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            System.err.println("Error: " + ex);
        }
    }

    private void vaciarCampos(){
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                Object o = field.get(this);
                if (!(o instanceof TextField)) continue;
                ((TextField) o).clear();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        imageOfCircle = new Image("/images/userIcon.png",150,150,false,true);
        circleImage.setFill(new ImagePattern(imageOfCircle));
    }

    @FXML
    private void revisarCcv(InputMethodEvent event) {
        if(ccvText.getText().length()!=3 ){
                ccvText.setStyle("-fx-text-fill: red");
            }
    }

    @FXML
    private void cambiarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Imágenes", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(
        ((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image img = new Image(selectedFile.toURI().toURL().toExternalForm(),150,150,false,true);
                imageOfCircle = img;
                circleImage.setFill(new ImagePattern(img));
            } catch (MalformedURLException ex) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("GreenBall Informa");
                dialog.setHeaderText("Error al cargar la imagen");
                dialog.showAndWait();
            }
            
        }
        
    }
    
}

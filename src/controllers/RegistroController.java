/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.Background;
import javafxmlapplication.GreenBallApp;
import model.Club;
import model.ClubDAOException;
import model.Member;

import javafxmlapplication.Scenes;

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
     private Club club;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            club = Club.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        registroButton.disableProperty().bind(Bindings.not(Bindings.createBooleanBinding(() ->
                    nombreProperty.get() && apellidosProperty.get() && telefonoProperty.get() && nicknameProperty.get() &&
                            passwordProperty.get() && tarjetaProperty.get(),
                nombreProperty, apellidosProperty, telefonoProperty, nicknameProperty, passwordProperty, tarjetaProperty
        )));
        
        List<Member> miembros = club.getMembers();
        List<String> nicknames = new ArrayList<>();
        miembros.forEach(member -> nicknames.add(member.getNickName()));

        nombreText.textProperty().addListener((a,b,c) -> {
            try{
                nombreText.setText(String.valueOf(nombreText.getText().charAt(0)).toUpperCase()+nombreText.getText().substring(1));
            } catch(Exception e){
                e.printStackTrace();
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
            try{
                apellidosText.setText(String.valueOf(apellidosText.getText().charAt(0)).toUpperCase()+apellidosText.getText().substring(1));
            } catch(Exception e){
                e.printStackTrace();
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

            if (c.isEmpty()) {
                tarjetaProperty.set(true);
                return;
            }

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
            } else {
                tarjetaText.setStyle("-fx-text-fill: red");
            }

            tarjetaProperty.set(ok);
            ccvText.setDisable(!ok);
        });
        
        ccvText.textProperty().addListener((a,b,c) -> {
            boolean ok = true;

            if (c.isEmpty() && tarjetaText.getText().isEmpty()) {
                tarjetaProperty.set(true);
                return;
            }

            if (ccvText.getText().length()!=3) {
                ccvErrLabel.setText("El código debe tener 3 números");
                ok = false;
            }
            if(!ccvText.getText().matches("[0-9]*" )){
                ccvErrLabel.setText("El código debe ser numérico");
                ok = false;
            }
            if(ok){
                ccvText.setStyle("-fx-text-fill: black");
                ccvErrLabel.setText("");
            } else {
                ccvText.setStyle("-fx-text-fill: red");
            }

            tarjetaProperty.set(ok);
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
    }

    @FXML
    private void registrarAction(ActionEvent event) {
        /*try {
            club = Club.getInstance();
            
            String name, surname,telephon, login, password, creditCard, imagepath;
            int svc;
            
            if(ccvText.getText().length()!=3 ){
                throw new NumberLengthException("Longitud erronea");
            }
                
            name = nombreText.getText();
            surname = apellidosText.getText();
            telephon = telText.getText();
            login = nickText.getText();
            password = contText.getText();
            creditCard = tarjetaText.getText();
            svc = Integer.parseInt(ccvText.getText());
            
            
            
            Member result = club.registerMember(name, surname, telephon, login, password, creditCard, svc, null);
            
            
        } catch (ClubDAOException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NumberLengthException ex){
            System.err.println("Error de numero: " + ex);
        }catch (Exception ex){
            System.err.println("Error: " + ex);
        }*/
    }

    @FXML
    private void revisarCcv(InputMethodEvent event) {
        if(ccvText.getText().length()!=3 ){
                ccvText.setStyle("-fx-text-fill: red");
            }
    }
    
    public class NumberLengthException extends RuntimeException{
        public NumberLengthException(String errMsg){
            super(errMsg);
        }
    }
}

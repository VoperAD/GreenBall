/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;

public class RegistroController implements Initializable {

    @FXML
    private Button volverButtton;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
    }
    
}

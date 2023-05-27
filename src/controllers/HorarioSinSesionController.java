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
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;

/**
 * FXML Controller class
 *
 * @author Sabin
 */
public class HorarioSinSesionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void volverAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
    }
    
}

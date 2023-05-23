package controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Club;
import model.ClubDAOException;

/**
 * FXML Controller class
 *
 * @author Sabin
 */
public class InicioController implements Initializable {

    @FXML
    private Button inicioSesionButton;
    @FXML
    private Button RegistroButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Club club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void InicioSesionAction(ActionEvent event) {
    }

    @FXML
    private void registroAction(ActionEvent event) {
    }
    
}

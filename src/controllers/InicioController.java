package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafxmlapplication.GreenBallApp;
import model.Club;
import model.ClubDAOException;

public class InicioController implements Initializable {

    @FXML
    private Button inicioSesionButton;
    @FXML
    private Button RegistroButton;
    @FXML
    private Button horariosButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Club club = Club.getInstance();
        } catch (ClubDAOException | IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void InicioSesionAction(ActionEvent event) {
        GreenBallApp.setRoot("signup");
    }

    @FXML
    private void registroAction(ActionEvent event) {
        GreenBallApp.setRoot("registro");
    }

    @FXML
    private void verHorarioAction(ActionEvent event) {
        GreenBallApp.setRoot("horariosinsesion");
    }
    
}

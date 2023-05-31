package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Club;
import model.ClubDAOException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InicioController implements Initializable {

    @FXML
    private Button inicioSesionButton;
    @FXML
    private Button registroButton;
    @FXML
    private Button horariosButton;

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
    private void inicioSesionAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.LOGIN);
    }

    @FXML
    private void registroAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.REGISTRO);
    }

    @FXML
    private void verHorarioAction(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.HORARIOS_SIN_SESION);
    }
    
}

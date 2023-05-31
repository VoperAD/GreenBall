package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Booking;
import model.Club;
import model.Court;

/**
 * FXML Controller class
 *
 * @author Fran
 */
public class MainViewController implements Initializable {

    @FXML
    private DatePicker selectedDate;
    @FXML
    private ListView<String> timeTable;
    
    private ObservableList<String> datos = null;

    private List<Court> pistas;
    private List<Label> pistasLabel;
    private List<Label> pistasLabelRes;
    private List<String> nombres;
    @FXML
    private Label lab1;
    @FXML
    private Label lab2;
    @FXML
    private Label lab3;
    @FXML
    private Label lab4;
    @FXML
    private Label lab5;
    @FXML
    private Label lab6;
    @FXML
    private Label lab1res;
    @FXML
    private Label lab2res;
    @FXML
    private Label lab3res;
    @FXML
    private Label lab4res;
    @FXML
    private Label lab5res;
    @FXML
    private Label lab6res;
    @FXML
    private Circle circleImage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    ArrayList<String> hours = new ArrayList<String>();
    hours.add("09:00");hours.add("10:00");hours.add("11:00");
    hours.add("12:00");hours.add("13:00");hours.add("14:00");
    hours.add("15:00");hours.add("16:00");hours.add("17:00");
    hours.add("18:00");hours.add("19:00");hours.add("20:00");
    hours.add("21:00");hours.add("22:00");

    datos = FXCollections.observableArrayList(hours);
    timeTable.setItems(datos);

    //No modificable, solo se muestra el horario de hoy
    selectedDate.setValue(LocalDate.now());

    //Creación de arrays auxiliares
    pistasLabel = new ArrayList<>();
    pistasLabel.add(lab1);pistasLabel.add(lab2);pistasLabel.add(lab3);
    pistasLabel.add(lab4);pistasLabel.add(lab5);pistasLabel.add(lab6);

    pistasLabelRes = new ArrayList<>();
    pistasLabelRes.add(lab1res);pistasLabelRes.add(lab2res);pistasLabelRes.add(lab3res);
    pistasLabelRes.add(lab4res);pistasLabelRes.add(lab5res);pistasLabelRes.add(lab6res);

    Club club = GreenBallApp.getClub();
    ArrayList<Booking> books = club.getBookings();

        pistas = club.getCourts(); 
        for(int i=0;i<pistas.size();i++){
            pistasLabel.get(i).setText(pistas.get(i).getName());
        }

        nombres = new ArrayList<>();
        pistasLabel.forEach(a -> nombres.add(a.getText()));

        //Aqui, el listener que actualiza las pistas en función de la hora elegida, al final
        //de initialize se seleccionan las 09:00
        timeTable.getSelectionModel().selectedItemProperty().addListener((a) -> {
            pistasLabelRes.forEach(b -> b.setText(""));
            for(int i = 0; i < books.size();i++){
                if(books.get(i).getMadeForDay().equals(LocalDate.now()) && books.get(i).getFromTime().equals(
                   LocalTime.of(Integer.parseInt(timeTable.getSelectionModel().getSelectedItem().substring(0, 2)), 0))){
                   int pistaIndex = nombres.indexOf(books.get(i).getCourt().getName());
                   pistasLabelRes.get(pistaIndex).setText("Reservada por: "+books.get(i).getMember().getNickName());
                   //para compropar si están bien, falta añadir efecto de ocupado
                   //Booking c = books.get(i);
                   //System.out.println(c.getCourt().getName() + " " +c.getFromTime()+" "+c.getMadeForDay()+" "+c.getMember().getNickName());
                }
            }
        });
        timeTable.getSelectionModel().select(0);
        circleImage.setFill(new ImagePattern(new Image("/images/userIcon.png",150,150,false,true)));
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.LOGIN);
    }

    @FXML
    private void volverInicio(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.INICIO);
    }
    
}

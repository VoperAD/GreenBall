/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

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


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ArrayList<String> hours = new ArrayList<String>();
        hours.add("9:00");hours.add("10:00");hours.add("11:00");
        hours.add("12:00");hours.add("13:00");hours.add("14:00");
        hours.add("15:00");hours.add("16:00");hours.add("17:00");
        hours.add("18:00");hours.add("19:00");hours.add("20:00");
        hours.add("21:00");hours.add("22:00");
        
        datos = FXCollections.observableArrayList(hours);
        timeTable.setItems(datos);
    }    
    
}

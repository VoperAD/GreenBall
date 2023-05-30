
package controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Booking;
import model.Court;
import model.Member;

import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author Fran
 */
public class HorarioSesionController implements Initializable {

    @FXML
    private RadioButton pistaUnoButton;
    @FXML
    private RadioButton pistaDosButton;
    @FXML
    private RadioButton pistaTresButton;
    @FXML
    private RadioButton pistaCuatroButton;
    @FXML
    private RadioButton pistaCincoButton;
    @FXML
    private RadioButton pistaSeisButton;
    @FXML
    private Label pistaUnoDisp;
    @FXML
    private Label pistaDosDisp;
    @FXML
    private Label pistaTresDisp;
    @FXML
    private Label pistaCuatroDisp;
    @FXML
    private Label pistaCincoDisp;
    @FXML
    private Label pistaSeisDisp;
    @FXML
    private ToggleGroup pistasToggleGroup;
    @FXML
    private Button reservarButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<LocalTime> timeTable;
    @FXML
    private Label reservaUno;
    @FXML
    private Label reservaDos;
    @FXML
    private Label reservaTres;
    @FXML
    private Label reservaCuatro;
    @FXML
    private Label reservaCinco;
    @FXML
    private Label reservaSeis;

    private ObservableList<LocalTime> datos = null;

    private List<Court> pistas;
    private List<Label> pistasLabel;
    private List<Label> pistasLabelRes;
    private List<String> nombres;
    private Map<Label, Label> dispReservaMap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pistasLabelRes = new ArrayList<>();
        pistasLabelRes.add(reservaUno);pistasLabelRes.add(reservaDos);pistasLabelRes.add(reservaTres);
        pistasLabelRes.add(reservaCuatro);pistasLabelRes.add(reservaCinco);pistasLabelRes.add(reservaSeis);

        dispReservaMap = new HashMap<>(Map.of(
                reservaUno, pistaUnoDisp,
                reservaDos, pistaDosDisp,
                reservaTres, pistaTresDisp,
                reservaCuatro, pistaCuatroDisp,
                reservaCinco, pistaCincoDisp,
                reservaSeis, pistaSeisDisp
        ));

        this.setCourtsName();
        this.initReservaLabelsBindings();
        this.initDispBindingsAndListeners();

        // Deshabilitamos la selección de días anteriores
        Callback<DatePicker, DateCell> dayCellFactory = param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #dddddd;");
                }
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);

        //No modificable, solo se muestra el horario de hoy
        datePicker.setValue(LocalDate.now());

        // Inicializamos el contenido del listView
        ArrayList<LocalTime> hours = new ArrayList<>();
        for (int i = 9; i < 22; i++) {
            hours.add(LocalTime.of(i, 0));
        }

        datos = FXCollections.observableArrayList(hours);
        timeTable.setItems(datos);

        ArrayList<Booking> bookings = GreenBallApp.getClub().getBookings();

        timeTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            LocalDate selectedDateValue = datePicker.getValue();
            List<Booking> currentBookings = bookings.stream()
                    .filter(booking -> booking.getMadeForDay().isEqual(selectedDateValue))
                    .filter(booking -> booking.getFromTime().equals(newValue))
                    .collect(Collectors.toList());

            pistasLabelRes.forEach(p -> p.setText(""));
            if (currentBookings.isEmpty()) return;

            for (Booking currentBooking : currentBookings) {
                String courtName = currentBooking.getCourt().getName();
                String reservada = "Reservada por: " + currentBooking.getMember().getNickName();
                if (courtName.equals(pistaUnoButton.getText())) reservaUno.setText(reservada);
                if (courtName.equals(pistaDosButton.getText())) reservaDos.setText(reservada);
                if (courtName.equals(pistaTresButton.getText())) reservaTres.setText(reservada);
                if (courtName.equals(pistaCuatroButton.getText())) reservaCuatro.setText(reservada);
                if (courtName.equals(pistaCincoButton.getText())) reservaCinco.setText(reservada);
                if (courtName.equals(pistaSeisButton.getText())) reservaSeis.setText(reservada);
            }
        });

        timeTable.getSelectionModel().select(0);
    }    

    @FXML
    private void volverInicio(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.USER);
    }

    @FXML
    private void onReservar(ActionEvent event) {
        LocalTime time = timeTable.getSelectionModel().getSelectedItem();
        LocalDate day = datePicker.valueProperty().get();
        if (time == null) return;

        Member user = GreenBallApp.getUser();
        List<Booking> userBookings = GreenBallApp.getClub().getUserBookings(user.getNickName()).stream()
                .filter(booking -> booking.getMadeForDay().isEqual(day))
                .collect(Collectors.toList());



    }

    private void setCourtsName() {
        List<Court> courts = GreenBallApp.getClub().getCourts();
        pistaUnoButton.setText(courts.get(0).getName());
        pistaDosButton.setText(courts.get(1).getName());
        pistaTresButton.setText(courts.get(2).getName());
        pistaCuatroButton.setText(courts.get(3).getName());
        pistaCincoButton.setText(courts.get(4).getName());
        pistaSeisButton.setText(courts.get(5).getName());
    }

    private void initReservaLabelsBindings() {
        for (Field declaredField : getClass().getDeclaredFields()) {
            try {
                if (!declaredField.getName().startsWith("reserva")) continue;
                Object o = declaredField.get(this);
                if (!(o instanceof Label)) continue;
                ((Label) o).visibleProperty().bind(Bindings.isNull(timeTable.getSelectionModel().selectedItemProperty()).not());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDispBindingsAndListeners() {
        for (Map.Entry<Label, Label> entry: dispReservaMap.entrySet()) {
            Label reserva = entry.getKey();
            Label disp = entry.getValue();
            disp.visibleProperty().bind(reserva.textProperty().isEmpty());
            reserva.textProperty().addListener((obs, old, newValue) -> {
                if (newValue.isBlank()) {
                    disp.setText("Disponible");
                } else {
                    disp.setText("Indisponible");
                }
            });
        }
    }

}

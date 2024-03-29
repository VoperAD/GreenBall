package controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Booking;
import model.ClubDAOException;
import model.Court;
import model.Member;

import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.AlertUtils;

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
    private List<Label> pistasLabelRes;
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
        this.initDispBindings();
        this.initRadioButtonsBindings();

        // Binding del botón de reservar -> solo se activa cuando se selecciona hora y pista
        reservarButton.disableProperty().bind(Bindings.or(
                pistasToggleGroup.selectedToggleProperty().isNull(),
                Bindings.isNull(timeTable.getSelectionModel().selectedItemProperty()))
        );

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
        datePicker.setValue(LocalDate.now());

        // Inicializamos el contenido del listView
        ArrayList<LocalTime> hours = new ArrayList<>();
        for (int i = 9; i < 22; i++) {
            hours.add(LocalTime.of(i, 0));
        }

        datos = FXCollections.observableArrayList(hours);
        timeTable.setItems(datos);

        ArrayList<Booking> bookings = GreenBallApp.getClub().getBookings();

        // List view listener
        timeTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            LocalDate selectedDateValue = datePicker.getValue();
            List<Booking> currentBookings = bookings.stream()
                    .filter(booking -> booking.getMadeForDay().isEqual(selectedDateValue))
                    .filter(booking -> booking.getFromTime().equals(newValue))
                    .collect(Collectors.toList());

            pistasLabelRes.forEach(p -> p.setText(""));
            pistasToggleGroup.selectToggle(null);
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

        datePicker.valueProperty().addListener((obs, old, newValue) -> {
            timeTable.getItems().clear();
            timeTable.getItems().addAll(hours);

            int ret = timeTable.getSelectionModel().getSelectedIndex();
            timeTable.getSelectionModel().select(ret + 1 >= datos.size() ? 0 : datos.size() - 1);
            timeTable.getSelectionModel().select(ret);
        });

        timeTable.setCellFactory(localTimeListView -> new ListCell<>() {
            @Override
            protected void updateItem(LocalTime localTime, boolean b) {
                super.updateItem(localTime, b);
                if (b || localTime == null) {
                    setDisable(false);
                    setText("");
                    setStyle("");
                } else {
                    if (localTime.isBefore(LocalTime.now()) && datePicker.getValue().isEqual(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #dddddd;");
                    } else {
                        setDisable(false);
                        setStyle("");
                    }
                    setText(localTime.toString());
                }
            }
        });

        timeTable.getSelectionModel().select(null);
    }    

    @FXML
    private void volverInicio(ActionEvent event) {
        GreenBallApp.setRoot(Scenes.USER);
    }

    @FXML
    private void onReservar(ActionEvent event) throws ClubDAOException {
        LocalTime time = timeTable.getSelectionModel().getSelectedItem();
        LocalDate day = datePicker.valueProperty().get();
        Toggle selectedToggle = pistasToggleGroup.getSelectedToggle();

        if (!(selectedToggle instanceof RadioButton)) return;
        RadioButton button = (RadioButton) selectedToggle;
        String pista = button.getText();

        // Guarda en una lista todas las reservas que tiene el usuario en ese mismo día y en la pista seleccionada
        Member user = GreenBallApp.getUser();
        List<LocalTime> horarios = GreenBallApp.getClub().getUserBookings(user.getNickName()).stream()
                .filter(booking -> booking.getMadeForDay().isEqual(day))
                .filter(booking -> booking.getCourt().getName().equals(pista))
                .map(Booking::getFromTime)
                .collect(Collectors.toList());

        horarios.add(time);
        Collections.sort(horarios);

        if (hasSequenceOfThree(horarios)) {
            // No puedes reservar una pista por más de dos horas seguidas
            Alert dialog = AlertUtils.createAlert(Alert.AlertType.ERROR, "Límite de reservas superado", "No pudes reservar la misma pista por más de 3 horas consecutivas");
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/estilos/global-style.css").toExternalForm());
            //Asigna la clase .myAlert al contenedor principal del diálogo
            dialog.getDialogPane().getStyleClass().add("info");
            dialog.showAndWait();
            return;
        }

        // Registra la reserva
        Booking result = GreenBallApp.getClub().registerBooking(
                LocalDateTime.now(),
                day,
                time,
                user.checkHasCreditInfo(),
                GreenBallApp.getClub().getCourt(pista),
                user
        );

        if (result == null) {
            throw new RuntimeException("Error al realizar la reserva Booking = null");
        }

        GreenBallApp.setRoot(Scenes.HORARIOS_CON_SESION);
        int ret = timeTable.getSelectionModel().getSelectedIndex();
        timeTable.getSelectionModel().select(ret + 1 > datos.size() ? 0 : datos.size() - 1);
        timeTable.getSelectionModel().select(ret);
    }

    public boolean hasSequenceOfThree(List<LocalTime> times) {
        if (times.size() < 3) {
            return false;
        }

        for (int i = 0; i < times.size() - 2; i++) {
            LocalTime current = times.get(i);
            LocalTime next = times.get(i + 1);
            LocalTime afterNext = times.get(i + 2);

            if (current.plusHours(1).equals(next) && next.plusHours(1).equals(afterNext)) {
                return true;
            }
        }

        return false;
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

    private void initDispBindings() {
        for (Map.Entry<Label, Label> entry: dispReservaMap.entrySet()) {
            Label reserva = entry.getKey();
            Label disp = entry.getValue();
            disp.visibleProperty().bind(Bindings.and(
                    reserva.textProperty().isEmpty(),
                    Bindings.isNull(timeTable.getSelectionModel().selectedItemProperty()).not()
            ));
        }
    }

    private void initRadioButtonsBindings() {
        Map<RadioButton, Label> buttons = new HashMap<>();
        buttons.put(pistaUnoButton, reservaUno);
        buttons.put(pistaDosButton, reservaDos);
        buttons.put(pistaTresButton, reservaTres);
        buttons.put(pistaCuatroButton, reservaCuatro);
        buttons.put(pistaCincoButton, reservaCinco);
        buttons.put(pistaSeisButton, reservaSeis);
        buttons.forEach((radioButton, label) -> radioButton.disableProperty().bind(Bindings.or(
                        label.textProperty().isEmpty().not(),
                        Bindings.isNull(timeTable.getSelectionModel().selectedItemProperty())))
        );
    }

}

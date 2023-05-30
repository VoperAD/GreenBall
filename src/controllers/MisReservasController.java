package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Booking;
import model.ClubDAOException;
import model.Member;
import utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MisReservasController implements Initializable {

    @FXML
    private Label holaText;
    @FXML
    private Button volverButton;
    @FXML
    private Button anularButton;
    @FXML
    private TableView<Booking> tableView;
    @FXML
    private TableColumn<Booking, String> dayColumn;
    @FXML
    private TableColumn<Booking, LocalTime> startTimeColumn;
    @FXML
    private TableColumn<Booking, LocalTime> endTimeColumn;
    @FXML
    private TableColumn<Booking, String> pistaColumn;
    @FXML
    private TableColumn<Booking, Boolean> isPaidColumn;

    private ObservableList<Booking> bookings;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volverButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.USER));
        anularButton.disableProperty().bind(Bindings.isNull(tableView.getSelectionModel().selectedItemProperty()));

        Member user = GreenBallApp.getUser();
        if (user == null) {
            throw new IllegalStateException("No se ha establecido el user global correctamente");
        }

        // Valores para cada columna
        dayColumn.setCellValueFactory(booking -> new SimpleStringProperty(booking.getValue().getMadeForDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        startTimeColumn.setCellValueFactory(booking -> new SimpleObjectProperty<>(booking.getValue().getFromTime()));
        endTimeColumn.setCellValueFactory(booking -> new SimpleObjectProperty<>(booking.getValue().getFromTime().plusHours(1L)));
        pistaColumn.setCellValueFactory(booking -> new SimpleStringProperty(booking.getValue().getCourt().getName()));
        isPaidColumn.setCellValueFactory(booking -> new SimpleBooleanProperty(booking.getValue().getPaid()));
        isPaidColumn.setCellFactory(c -> new PaidCell());

        List<Booking> userBookingsOrdered = GreenBallApp.getClub().getUserBookings(user.getNickName()).stream()
                .sorted(Booking::compareTo)
                .filter(booking -> !booking.getMadeForDay().isBefore(LocalDate.now()))
                .filter(booking -> !LocalTime.now().isBefore(booking.getFromTime().plusHours(1)))
                .collect(Collectors.toList());

        if (userBookingsOrdered.size() > 10) {
            bookings = FXCollections.observableArrayList(userBookingsOrdered.subList(0, 10));
            userBookingsOrdered.removeAll(userBookingsOrdered.subList(0, 10));
        } else {
            bookings = FXCollections.observableArrayList(userBookingsOrdered);
            userBookingsOrdered.clear();
        }

        bookings.addListener((ListChangeListener<? super Booking>) change -> {
            if (!userBookingsOrdered.isEmpty()) bookings.add(userBookingsOrdered.get(0));
        });

        tableView.setItems(bookings);
    }

    @FXML
    private void onAnularReserva(ActionEvent event) throws ClubDAOException {
        Booking selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        LocalTime fromTime = selectedItem.getFromTime();
        LocalDate madeForDay = selectedItem.getMadeForDay();
        LocalDate now = LocalDate.now();

        Alert alert = AlertUtils.createAlert(Alert.AlertType.ERROR, "No ha sido posible anular la reserva!", "No se puede cancelar una reserva con menos de 24 horas de antelación");

        // Mismo día, no se puede cancelar
        if (now.isEqual(madeForDay)) {
            alert.showAndWait();
            return;
        }

        // Queda menos de 24 horas para la reserva
        if (now.until(madeForDay).getDays() == 1 && LocalTime.now().isAfter(fromTime)) {
            alert.showAndWait();
            return;
        }

        Alert confirm = AlertUtils.createAlert(Alert.AlertType.CONFIRMATION, "Estás seguro de que quieres cancelar esta reserva?",
                "Fecha: " + madeForDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n" +
                "Hora: " + fromTime + "\n" +
                "Pista: " + selectedItem.getCourt().getName()
        );

        ButtonType si = new ButtonType("Sí");
        ButtonType no = new ButtonType("No");
        confirm.getButtonTypes().clear();
        confirm.getButtonTypes().addAll(si, no);

        Optional<ButtonType> buttonType = confirm.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == si) {
            bookings.remove(selectedItem);
            GreenBallApp.getClub().removeBooking(selectedItem);
        }

    }

    private static class PaidCell extends TableCell<Booking, Boolean> {
        private final ImageView view = new ImageView();

        @Override
        protected void updateItem(Boolean aBoolean, boolean empty) {
            super.updateItem(aBoolean, empty);
            if (aBoolean == null || empty) {
                setGraphic(null);
                setText(null);
            } else {
                Image image = aBoolean ? new Image("images/yes-check.png", 15, 15, true, true) : new Image("images/no-check.png", 15, 15, true, true);
                view.setImage(image);
                setGraphic(view);
            }
        }
    }

}

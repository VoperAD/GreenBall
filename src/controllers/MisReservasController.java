package controllers;

import javafx.application.Platform;
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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafxmlapplication.GreenBallApp;
import javafxmlapplication.Scenes;
import model.Booking;
import model.Club;
import model.ClubDAOException;
import model.Member;
import utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.stage.Stage;

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
    @FXML
    private Circle circleImage;

    private ObservableList<Booking> bookings = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Member user = GreenBallApp.getUser();
        if (user == null) {
            throw new IllegalStateException("No se ha establecido el user global correctamente");
        }

        this.initTableColumns();
        this.setupTableViewPlaceholder();
        volverButton.setOnAction(event -> GreenBallApp.setRoot(Scenes.USER));
        anularButton.disableProperty().bind(Bindings.isNull(tableView.getSelectionModel().selectedItemProperty()));
        holaText.setText(holaText.getText() + user.getName() + "!");

        // Configuración de la visualización de los bookings
        List<Booking> userBookingsOrdered = GreenBallApp.getClub().getUserBookings(user.getNickName()).stream()
                .sorted(Booking::compareTo)
                .filter(b -> !b.getMadeForDay().isBefore(LocalDate.now()))
                .filter(b -> !b.getMadeForDay().isEqual(LocalDate.now()) || !LocalTime.now().isAfter(b.getFromTime().plusMinutes(GreenBallApp.getClub().getBookingDuration())))
                .collect(Collectors.toList());

        List<Booking> firstTen = userBookingsOrdered.size() > 10 ? userBookingsOrdered.subList(0, 10) : userBookingsOrdered;
        bookings = FXCollections.observableArrayList(firstTen);

        if (userBookingsOrdered.size() >= 10) {
            userBookingsOrdered.subList(0, 10).clear();
        } else {
            userBookingsOrdered.clear();
        }

        bookings.addListener((ListChangeListener<? super Booking>) change -> {
            while (change.next()) {
                if (!change.wasRemoved()) return;
                Platform.runLater(() -> {
                    if (!userBookingsOrdered.isEmpty()) {
                        Booking booking = userBookingsOrdered.get(0);
                        bookings.add(booking);
                        userBookingsOrdered.remove(booking);
                    };
                });
            }
        });

        tableView.setItems(bookings);
        Image img = GreenBallApp.getUser().getImage();
        circleImage.setFill(new ImagePattern(img));
    }

    @FXML
    private void onAnularReserva(ActionEvent event) throws ClubDAOException {
        Booking selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        LocalTime fromTime = selectedItem.getFromTime();
        LocalDate madeForDay = selectedItem.getMadeForDay();
        LocalDate now = LocalDate.now();

        // Mismo día, no se puede cancelar
        if (now.isEqual(madeForDay) || (now.until(madeForDay).getDays() == 1 && LocalTime.now().isAfter(fromTime))) {
            Alert alert = AlertUtils.createAlert(Alert.AlertType.ERROR, "No ha sido posible anular la reserva!", "No se puede cancelar una reserva con menos de 24 horas de antelación");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
            getClass().getResource("/estilos/global-style.css").toExternalForm());
            //Asigna la clase .myAlert al contenedor principal del diálogo
            alert.getDialogPane().getStyleClass().add("info");
            alert.showAndWait();
            return;
        }

        Alert confirm = AlertUtils.createAlert(Alert.AlertType.CONFIRMATION, "Estás seguro de que quieres cancelar esta reserva?",
                "Fecha: " + madeForDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n" +
                "Hora: " + fromTime + "\n" +
                "Pista: " + selectedItem.getCourt().getName()
        );
        Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        DialogPane dialogPane = confirm.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/estilos/global-style.css").toExternalForm());
        //Asigna la clase .myAlert al contenedor principal del diálogo
        confirm.getDialogPane().getStyleClass().add("info");

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

    @FXML
    private void onAnularTodas(ActionEvent event) throws ClubDAOException {
        Member user = GreenBallApp.getUser();
        Club club = GreenBallApp.getClub();

        List<Booking> validBookings = club.getUserBookings(user.getNickName()).stream()
                .filter(booking -> LocalDate.now().until(booking.getMadeForDay()).getDays() > 1 ||
                        LocalDate.now().until(booking.getMadeForDay()).getDays() == 1 && !LocalTime.now().isAfter(booking.getFromTime()))
                .collect(Collectors.toList());

        if (validBookings.isEmpty()) {
            Alert fail = AlertUtils.createAlert(Alert.AlertType.ERROR, "No existen reservas que se puedan cancelar!");
            Stage stage = (Stage) fail.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            DialogPane dialogPane = fail.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/estilos/global-style.css").toExternalForm());
            fail.getDialogPane().getStyleClass().add("info");
            fail.showAndWait();
            return;
        }

        Alert alert = AlertUtils.createAlert(Alert.AlertType.CONFIRMATION,
                "Estás seguro de que quieres cancelar todas tus reservas?",
                "Se cancelarán todas las reservas que no estén dentro de las próximas 24 horas! " +
                        "Atención: esta es una acción irreversible!");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/estilos/global-style.css").toExternalForm());
        //Asigna la clase .myAlert al contenedor principal del diálogo
        alert.getDialogPane().getStyleClass().add("info");

        ButtonType si = new ButtonType("Sí");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(si, no);

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == si) {
            for (Booking validBooking : validBookings) {
                bookings.remove(validBooking);
                club.removeBooking(validBooking);
            }
        }

    }

    private void initTableColumns() {
        // Valores para cada columna
        dayColumn.setCellValueFactory(booking -> new SimpleStringProperty(booking.getValue().getMadeForDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        startTimeColumn.setCellValueFactory(booking -> new SimpleObjectProperty<>(booking.getValue().getFromTime()));
        endTimeColumn.setCellValueFactory(booking -> new SimpleObjectProperty<>(booking.getValue().getFromTime().plusHours(1L)));
        pistaColumn.setCellValueFactory(booking -> new SimpleStringProperty(booking.getValue().getCourt().getName()));
        isPaidColumn.setCellValueFactory(booking -> new SimpleBooleanProperty(booking.getValue().getPaid()));
        isPaidColumn.setCellFactory(c -> new PaidCell());
    }

    private void setupTableViewPlaceholder() {
        ImageView imageView = new ImageView("images/infoIcon.png");
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        Label textLabel = new Label("Todavía no tienes ninguna reserva.");
        textLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        VBox placeholderBox = new VBox(imageView, textLabel);
        placeholderBox.setAlignment(Pos.CENTER);
        placeholderBox.setSpacing(15);

        tableView.setPlaceholder(placeholderBox);
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

package utils;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static Alert createAlert(Alert.AlertType type, final String headerText, final String contentText) {
        Alert alert = new Alert(type, contentText);
        alert.setHeaderText(headerText);
        alert.setTitle("GreenBall informa");
        return alert;
    }

    public static Alert createAlert(Alert.AlertType type, final String headerText) {
        return createAlert(type, headerText, "");
    }

}

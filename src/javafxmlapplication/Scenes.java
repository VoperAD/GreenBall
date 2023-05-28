package javafxmlapplication;

public enum Scenes {

    INICIO("inicio"),
    LOGIN("login"),
    REGISTRO("registro"),
    USER("user"),
    HORARIOS_SIN_SESION("MainView");

    private final String filePath;

    Scenes(final String fileName) {
        this.filePath = "/vistas/" + fileName + ".fxml";
    }

    public String getFilePath() {
        return this.filePath;
    }

}

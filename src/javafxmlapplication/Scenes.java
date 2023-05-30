package javafxmlapplication;

public enum Scenes {

    INICIO("inicio"),
    LOGIN("login"),
    REGISTRO("registro"),
    USER("user"),
    HORARIOS_SIN_SESION("MainView"),
    HORARIOS_CON_SESION("testreservar", false),
    CONFIG_PERFIL("configPerfil", false),
    MIS_RESERVAS("misReservas", false);

    private final String filePath;
    // Debe cargarse al iniciar el programa?
    private final boolean loadOnLaunch;

    Scenes(final String fileName, final boolean loadOnLaunch) {
        this.filePath = "/vistas/" + fileName + ".fxml";
        this.loadOnLaunch = loadOnLaunch;
    }

    Scenes(final String fileName) {
        this(fileName, true);
    }

    public String getFilePath() {
        return this.filePath;
    }

    public boolean shouldLoadOnLaunch() {
        return this.loadOnLaunch;
    }

}

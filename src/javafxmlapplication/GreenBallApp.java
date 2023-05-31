package javafxmlapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;
import model.Member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GreenBallApp extends Application {

    public static final Map<Scenes, Parent> ROOTS = new HashMap<>();
    public static final Map<Scenes, ?> CONTROLLERS = new HashMap<>();

    private static Club club;
    private static Member user;
    private static Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Application.setUserAgentStylesheet("/estilos/global-style.css");

        // Cargamos las Scenes
        FXMLLoader loader;
        for (Scenes scene: Scenes.values()) {
            if (!scene.shouldLoadOnLaunch()) continue;
            loader = new FXMLLoader(getClass().getResource(scene.getFilePath()));
            Parent parent = loader.load();
            ROOTS.put(scene, parent);
            CONTROLLERS.put(scene, loader.getController());
            parent.getStylesheets().add(getClass().getResource("/estilos/global-style.css").toExternalForm());
        }

        mainScene = new Scene(ROOTS.get(Scenes.INICIO));
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setMinWidth(950); stage.setMinHeight(600);
        stage.setScene(mainScene);
        stage.setTitle("GreenBall");
        stage.show();


//        getClub().registerBooking(LocalDateTime.now(), LocalDate.of(2023, 9, 10), LocalTime.of(10, 0),true, getClub().getCourt("Pista 1"), getClub().getMemberByCredentials("user2", "123456x"));
    }

    // Método para resetear el estado de una scene
    public static void reloadScene(Scenes scene)  {
        FXMLLoader loader = new FXMLLoader(GreenBallApp.class.getResource(scene.getFilePath()));
        try {
            ROOTS.replace(scene, loader.load());
            CONTROLLERS.replace(scene, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(Scenes scene) {
        Parent parent = ROOTS.get(scene);
        if (parent == null) {
            try {
                FXMLLoader loader = new FXMLLoader(GreenBallApp.class.getResource(scene.getFilePath()));
                parent = loader.load();
                ROOTS.put(scene, parent);
                CONTROLLERS.put(scene, loader.getController());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainScene.setRoot(parent);
    }

    public static Club getClub() {
        try {
            if (club == null) club = Club.getInstance();
        } catch (ClubDAOException | IOException e) {
            e.printStackTrace();
        }
        return club;
    }

    public static Member getUser() {
        return user;
    }

    public static void setUser(Member member) {
        user = member;
    }

}


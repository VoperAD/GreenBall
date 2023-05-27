package javafxmlapplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;

public class GreenBallApp extends Application {

    public static final Map<Scenes, Parent> ROOTS = new HashMap<>();
    public static final Map<Scenes, ?> CONTROLLERS = new HashMap<>();

    private static Scene mainScene;
    private static Club club;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        /*
        Nota: Los roots se guardan en un hasmap, aqui en el inicio del stage creamos y guardamos los roots
        con todas las vistas para luego poder cargarlas mediante GreenBallApp.setRoot("escena"); , he usado nombres idénticos.
        Con esto se reutilizan escenas pero creo que no podemos hacer lo mismo para user debido a que sus datos se crean a partir
        del registro. Habria que crear nuevos roots cada vez.
        */
        
        FXMLLoader loader;

        for (Scenes scene: Scenes.values()) {
            loader = new FXMLLoader(getClass().getResource(scene.getFilePath()));
            Parent parent = loader.load();
            ROOTS.put(scene, parent);
            CONTROLLERS.put(scene, loader.getController());
        }

        mainScene = new Scene(ROOTS.get(Scenes.INICIO));
        stage.setScene(mainScene);
        stage.setTitle("GreenBall");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setRoot(Scenes newScene) {
        Parent parent = ROOTS.get(newScene);
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

}


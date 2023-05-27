package javafxmlapplication;

import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GreenBallApp extends Application {
    
    private static Scene scene;
    
    private static HashMap<String,Parent> roots = new HashMap<>();

    static void setRoot(Parent root) {
        scene.setRoot(root);
    }
    
    public static void setRoot(String clave){
        Parent root = roots.get(clave);
        if(root != null){
            setRoot(root);
            
        }else{
            System.err.println("Error al cargar la escena");
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        /*Nota: Los roots se guardan en un hasmap, aqui en el inicio del stage creamos y guardamos los roots
         con todas las vistas para luego poder cargarlas mediante GreenBallApp.setRoot("escena"); , he usado nombres idénticos.
         Con esto se reutilizan escenas pero creo que no podemos hacer lo mismo para user debido a que sus datos se crean a partir
         del registro. Habria que crear nuevos roots cada vez. */
        
        Parent root;
        FXMLLoader loader;
        
        loader= new  FXMLLoader(getClass().getResource("/vistas/login.fxml"));
        root = loader.load();
        roots.put("login", root);
        loader= new  FXMLLoader(getClass().getResource("/vistas/registro.fxml"));
        root = loader.load();
        roots.put("registro", root);
        loader= new  FXMLLoader(getClass().getResource("/vistas/user.fxml"));
        root = loader.load();
        roots.put("user", root);
        loader= new  FXMLLoader(getClass().getResource("/vistas/horariosinsesion.fxml"));
        root = loader.load();
        roots.put("horariosinsesion", root);
        
        //dejad esta la última
        loader= new  FXMLLoader(getClass().getResource("/vistas/inicio.fxml"));
        root = loader.load();
        roots.put("inicio", root);
        
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("GreenBall");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        
    }
}


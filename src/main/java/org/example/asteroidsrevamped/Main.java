package org.example.asteroidsrevamped;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Set up root node
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        // Setup scene
        Scene scene = new Scene(root);

        MainMenuController controller = loader.getController();
        controller.getSoundPool().playBackgroundMusicClip();

        // Change stage icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/asteroids-icon.png")));
        stage.getIcons().add(icon);


        // Release resources on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(controller::terminate));

        // Set stage
        stage.setTitle("Asteroids Revamped!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        System.setProperty("javafx.runtime.preview", "true");
        Application.launch(Main.class, args);
    }
}

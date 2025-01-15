package org.example.asteroidsrevamped;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


/**
 * This class is the main controller for the application. It handles switching between different scenes
 * (MainMenu, GameWindow, Scores) and manages the game wide sound pool.
 */
public class SceneController {

    private volatile boolean isCleanupComplete = false;

    private Stage stage;
    private Parent root;
    private Scene currentScene;

    // Keep track of loaded controllers for cleanup
    private GameController currentGameController;
    private MainMenuController currentStartController;

    ScoreManager scoreManager = new ScoreManager();

    // Game wide accessible sound pool
    private SoundPool soundPool;

    private static boolean restartBackgroundMusic;

    {
        try {
            soundPool = SoundPool.getInstance();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches the scene to the MainMenu.fxml scene.
     *
     * @param event the ActionEvent that triggered the scene switch
     * @throws IOException if an error occurs during loading the FXML file
     * @throws LineUnavailableException if there is an error acquiring a mixer line
     * @throws UnsupportedAudioFileException if the audio file format is not supported
     */
    @FXML
    public void switchToMainMenu(ActionEvent event) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // Cleanup previous scene if needed
        if (currentGameController != null) {
            currentGameController.cleanup();
            currentGameController = null;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        root = loader.load();

        currentStartController = loader.getController();

        if (restartBackgroundMusic) {
            this.getSoundPool().playBackgroundMusicClip();
        }
        // reset flag to restart background music
        restartBackgroundMusic = false;

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        if (currentScene != null) {
            currentScene.getStylesheets().clear();
        }
        currentScene = new Scene(root);

        currentStartController.initialize();

        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Switches the scene to the GameWindow.fxml scene.
     *
     * @param event the ActionEvent that triggered the scene switch
     * @throws IOException if an error occurs during loading the FXML file
     * @throws LineUnavailableException if there is an error acquiring a mixer line
     * @throws UnsupportedAudioFileException if the audio file format is not supported
     */
    @FXML
    public void switchToGameWindow(ActionEvent event) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // Only restart background music when playing a new game or visiting main menu after having played.
        restartBackgroundMusic = true;
        System.out.println(true);
        // Cleanup previous scene
        if (currentStartController != null) {
            currentStartController = null;
        }
        if (currentGameController != null) {
            currentGameController.cleanup();
            currentGameController = null;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
        root = loader.load();

        // Restart background music
        this.getSoundPool().playBackgroundMusicClip();

        if (currentScene != null) {
            currentScene.getStylesheets().clear();
        }
        currentScene = new Scene(root);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        currentGameController = loader.getController();
        Pane gamePane = currentGameController.getGamePane();
        currentGameController.initialize(gamePane);

        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Switches the scene to the Scores.fxml scene.
     *
     * @param event the ActionEvent that triggered the scene switch
     * @throws IOException if an error occurs during loading the FXML file
     */
    @FXML
    public void switchToScoreWindow(ActionEvent event) throws IOException {
        if (currentStartController != null) {
            currentStartController = null;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scores.fxml"));
        root = loader.load();

        if (currentScene != null) {
            currentScene.getStylesheets().clear();
        }
        currentScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Switches the scene to the PlayerControls.fxml scene.
     *
     * @param event the ActionEvent that triggered the scene switch
     * @throws IOException if an error occurs during loading the FXML file
     */
    @FXML
    public void switchToControlsWindow(ActionEvent event) throws IOException {
        if (currentStartController != null) {
            currentStartController = null;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerControls.fxml"));
        Parent root = loader.load();

        if (currentScene != null) {
            currentScene.getStylesheets().clear();
        }
        currentScene = new Scene(root);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Returns the game's sound pool.
     *
     * @return the SoundPool instance
     */
    public SoundPool getSoundPool() {
        return soundPool;
    }

    public Stage getStage() {
        return stage;
    }
    /**
     * Terminates the application by cleaning up resources and closing the stage.
     */
    public void terminateApplication() {
        if (!isCleanupComplete) {
            synchronized (this) {
                if (!isCleanupComplete) {
                    System.out.println("Starting application cleanup...");
                    // Cleanup game controller
                    if (currentGameController != null) {
                        currentGameController.cleanup();
                    }

                    // Clear sound resources
                    if (soundPool != null) {
                        soundPool.cleanup();
                        soundPool = null;
                    }

                    // Handle JavaFX operations on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        try {
                            // Clear resources
                            if (currentScene != null) {
                                currentScene.getStylesheets().clear();
                            }

                            // Close the stage
                            if (stage != null) {
                                stage.close();
                            }

                        } catch (Exception e) {
                            System.err.println("Error during application termination: " + e.getMessage());
                        }
                    });
                    isCleanupComplete = true;
                    System.out.println("Application cleanup completed");
                }
            }
        }
    }
}
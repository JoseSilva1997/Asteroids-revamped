package org.example.asteroidsrevamped;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * This class controls the main menu scene of the game.
 *
 * @author Jose Silva
 */
public class MainMenuController extends SceneController {

    public AnchorPane startMenuPane;
    public Label playerNameDisplay1;
    @FXML
    private TextField playerNameField;
    @FXML
    private Label playerNameDisplay;
    private static boolean playerHasEnteredName = false;

    /**
     * Initializes the main menu scene.
     * - Sets the displayed player name.
     * - Limits player name input to 16 characters.
     * - Handles Enter key press to update player name.
     */
    @FXML
    public void initialize() {
        if (!playerHasEnteredName) {
            playerNameDisplay.setText("Please enter your name");
        } else {
            playerNameDisplay.setText(Player.name);
        }
       if (playerNameField != null) {
           // Add a listener to limit the number of characters
           playerNameField.textProperty().addListener((observable, oldValue, newValue) -> {
               if (newValue.length() > 16) {
                   playerNameField.setText(newValue.substring(0, 16));
               }
           });

           playerNameField.setOnKeyPressed(event -> {
               if (event.getCode() == KeyCode.ENTER) {
                   playerNameDisplay.setText(playerNameField.getText());
                   Player.name = playerNameField.getText();
                   playerNameField.setText("");
                   playerNameDisplay.requestFocus();
                   playerHasEnteredName = true;
               }
           });
       }
    }

    /**
     * Terminates the application when the "Exit" button is clicked.
     */
    @FXML
    public void terminateButton() {
        Platform.exit();
    }

    /**
     * Call terminateApplication and exit the platform.
     */
    public void terminate() {
        terminateApplication();
        Platform.exit();
        System.out.println("Application terminated successfully");
    }
}

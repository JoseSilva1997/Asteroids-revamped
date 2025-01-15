package org.example.asteroidsrevamped;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/**
 * This class handles player input for controlling the spaceship and game state.
 *
 * @author Jose Silva
 */
public class PlayerControls {


    /**
     * Creates a new PlayerControls object, which registers event listeners for
     * handling player input.
     *
     * @param player The player object representing the player character.
     * @param gamePane The pane containing the game elements.
     * @param soundPool The sound pool for playing sound effects.
     * @param gameController The game controller for handling game logic.
     */
    public PlayerControls(Player player, Pane gamePane, SoundPool soundPool, GameController gameController) {
        gamePane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                player.getSpaceship().rotateLeft();
            }
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                player.getSpaceship().rotateRight();
            }
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                player.getSpaceship().moveFront();
            }
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                player.getSpaceship().moveBack();
            }
            if (event.getCode() == KeyCode.SPACE) {
                try {
                    player.shoot(soundPool);
                } catch (LineUnavailableException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (event.getCode() == KeyCode.B) {
                if(!Entity.visibleBounds) {
                    GameController.makeBoundsVisible();
                } else {
                    GameController.makeBoundsInvisible();
                }
            }
            // Pause the game
            if (event.getCode() == KeyCode.ENTER) {
                if (!GameController.gameIsPaused) {
                    gameController.pauseGame();
                } else {
                    gameController.resumeGame();
                }
            }
        });

        gamePane.getScene().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                player.getSpaceship().stopRotating();
            }
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                player.getSpaceship().stopRotating();
            }
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                player.getSpaceship().notMoveFront();
            }
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                player.getSpaceship().notMoveBack();
            }
        });
    }
}


package org.example.asteroidsrevamped;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the player character in the game.
 *
 * @author Jose Silva
 */
public class Player {

    /**
     * Number of nanoseconds in one second.
     */
    private static final double NANOS_PER_SECOND = 1000000000;

    /**
     * Default player name.
     */
    public static String name = "No name";

    /**
     * Player's spaceship.
     */
    private Spaceship spaceship;

    /**
     * Pane containing the game elements.
     */
    private final Pane gamePane;

    /**
     * Image views representing player lives.
     */
    private final ImageView[] playerLives;

    /**
     * Current number of player lives.
     */
    private int lives;

    /**
     * Player's current score.
     */
    private int score;

    /**
     * Time of the previous player shot (in nanoseconds).
     */
    private double previousShootingTime = -1;

    /**
     * Constructs a new Player object.
     *
     * @param gamePane The pane containing the game elements.
     */
    public Player(Pane gamePane) {
        this.gamePane = gamePane;
        this.lives = 3;
        this.score = 0;
        spaceship = new Spaceship(gamePane);
        playerLives = new ImageView[3]; // Initialize player lives display
    }

    public static String getName() {
        return name;
    }

    /**
     * Gets the player's spaceship.
     *
     * @return The player's spaceship.
     */
    public Spaceship getSpaceship() {
        return spaceship;
    }

    /**
     * Gets the number of player lives remaining.
     *
     * @return The number of player lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Gets the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Removes a life from the player.
     */
    public void removeLife() {
        this.lives--;
        gamePane.getChildren().remove(playerLives[lives]);
    }

    /**
     * Increases the player's score by a specified amount.
     *
     * @param amount The amount to increase the score.
     */
    public void increaseScore(int amount) {
        score += amount;
    }


    /**
     * Fires a laser from the player's spaceship.
     *
     * @param pool The sound pool to play the laser sound.
     * @throws LineUnavailableException If there is an error playing the sound.
     * @throws IOException              If there is an error loading the sound effect.
     */
    public void shoot(SoundPool pool) throws LineUnavailableException, IOException {
        if (!GameController.gameIsPaused) {
            double elapsedTime = System.nanoTime() - previousShootingTime;

            if (elapsedTime >= NANOS_PER_SECOND / 10) { // Limit firing rate to 1/0.1 seconds
                Laser laser = spaceship.shoot();
                if (laser != null) {
                    ImageView newLaser = laser.getView();
                    // Add laser to the scene if it's not already there
                    if (!gamePane.getChildren().contains(newLaser)) {
                        if (Entity.visibleBounds) {
                            laser.makeBoundsVisible(gamePane);// =============================================================================> Test bounds
                        }
                        gamePane.getChildren().add(newLaser);
                        spaceship.getAmmunition().setProgress(spaceship.getAmmunition().getProgress() - 0.1);
                    }
                    pool.playLaserSound();
                }
                previousShootingTime = System.nanoTime();
            }
        }
    }

    /**
     * Updates the position of the player's lasers.
     */
    public void updateLasers(double deltaTime) {
        ArrayList<Laser> lasersToRemove = new ArrayList<>();
        // Create animation for the laser
        for (Laser laser : spaceship.getLaserPool().getActiveLasers()) {
            double angleInRadians = Math.toRadians(laser.getRotation());
            double velocityX = Math.cos(angleInRadians) * laser.getLaserSpeed();
            double velocityY = Math.sin(angleInRadians) * laser.getLaserSpeed();
            ImageView laserView = laser.getView();

            // Update distance travelled over the past frame
            double distanceThisFrame = Math.sqrt(velocityX * velocityX + velocityY * velocityY) * deltaTime;
            laser.addDistanceTraveled(distanceThisFrame);

            double movLaserThisFrameX = laserView.getX() + velocityX * deltaTime;
            double movLaserThisFrameY = laserView.getY() + velocityY * deltaTime;
            double movBoundThisFrameX = laser.getBoundX() + velocityX * deltaTime;
            double movBoundThisFrameY = laser.getBoundY() + velocityY * deltaTime;

            laser.setPosition(movLaserThisFrameX, movLaserThisFrameY, movBoundThisFrameX, movBoundThisFrameY);

            if (laser.getDistanceTraveled() >= 1000) {
                laser.makeBoundsInvisible(gamePane);
                gamePane.getChildren().remove(laser.getView());
                spaceship.getLaserPool().getInactiveLasers().add(laser);
                lasersToRemove.add(laser);
                laser.resetDistanceTraveled();
            }
        }
        lasersToRemove.forEach(laser -> spaceship.getLaserPool().getActiveLasers().remove(laser));
        if (spaceship.getLaserPool().reloadLasers()) {
            spaceship.getAmmunition().setProgress(spaceship.getAmmunition().getProgress() + 0.1);
        }
    }

    /**
     * Creates and displays the player's lives on the game pane.
     */
    public void setLives() {
        for (int i = 0; i < playerLives.length; i++) {
            Image lifeIconImage = new Image(Objects.requireNonNull(Player.class.getResourceAsStream("/images/hearts.png")));
            ImageView lifeIcon = new ImageView(lifeIconImage);
            lifeIcon.setFitHeight(30);
            lifeIcon.setFitWidth(30);
            lifeIcon.setX(10 + i * 30); // Position lives in the top-left corner
            lifeIcon.setY(10);
            playerLives[i] = lifeIcon;
            gamePane.getChildren().add(lifeIcon);
        }
    }

    /**
     * Cleans up player resources.
     */
    public void cleanup() {

        spaceship.cleanup();
        spaceship = null;

        // Remove event handlers
        if (gamePane.getScene() != null) {
            gamePane.getScene().setOnKeyPressed(null);
            gamePane.getScene().setOnKeyReleased(null);
        }

        Arrays.fill(playerLives, null);

        System.out.println("Player cleanup complete");
    }
}

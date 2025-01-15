package org.example.asteroidsrevamped;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class controls the overall game flow and logic.
 * It manages the game loop, player, asteroids, collisions, and game state (level, score, lives).
 *
 * @author Jose Silva
 */
public class GameController extends SceneController {

    @FXML
    private Pane gamePane;
    // Constant representing the number of nanoseconds in one second
    private static final double NANOS_PER_SECOND = 1000000000;
    // Flag indicating whether the game is paused
    public static boolean gameIsPaused;

    public static final double WINDOW_WIDTH = 1080;
    public static final double WINDOW_HEIGHT = 800;

    // Get monitor refresh rate

    private static final double REFRESH_RATE = 240.0; // Cap at 240Hz.
    private static final double FIXED_TIME_STEP = 1.0 / REFRESH_RATE;
    private static final double NANOS_TO_SECONDS = 1.0 / 1_000_000_000.0;

    // Debug FPS counter
    private Label fpsCounter;
    private long frameCount = 0;
    private long lastFpsUpdate = 0;

    private double accumulator = 0.0;
    private long lastUpdateTime = 0;

    // Game labels
    private Label levelLabel;
    private Label scoreLabel;
    private Label pauseLabel;
    private Label gameOverLabel;

    // Game buttons
    private Button backButton;
    private Button restartButton;

    private Player player;

    // Asteroid attributes
    private AsteroidPool asteroidPool;
    // Tracks asteroid collisions with the ship
    private HashMap<Integer, Double> asteroidsThatHitShip;

    // Load level controller
    private LevelController levelController;



    // ================================================================================================
    //                                    Game loop
    // ================================================================================================

    /**
     * The main game loop that handles game updates on each frame. Capped at ≈ 60FPS.
     */
    private AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdate = 0;
        private double accumulator = 0;

        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }

            // Update FPS counter
            updateFpsCounter(now);

            double deltaTime = (now - lastUpdate) * NANOS_TO_SECONDS;
            lastUpdate = now;

            // Prevent spiral of death
            if (deltaTime > 0.25) {
                deltaTime = 0.25;
            }

            accumulator += deltaTime;

            // Update game logic at fixed time steps
            while (accumulator >= FIXED_TIME_STEP) {
                updateGame(FIXED_TIME_STEP);
                accumulator -= FIXED_TIME_STEP;
            }

            // Render with interpolation
            double alpha = accumulator / FIXED_TIME_STEP;
            render(alpha);
        }
    };

    private void updateFpsCounter(long now) {
        frameCount++;

        if (now - lastFpsUpdate >= NANOS_PER_SECOND) {
            double fps = frameCount * (NANOS_PER_SECOND / (double)(now - lastFpsUpdate));
            fpsCounter.setText(String.format("FPS: %.2f", fps));
            frameCount = 0;
            lastFpsUpdate = now;
        }
    }

    private void updateGame(double deltaTime) {
        gameOver();
        asteroidPool.updateAsteroids(deltaTime);
        player.getSpaceship().updatePosition(deltaTime);
        player.updateLasers(deltaTime);
        checkCollisions();
        levelUp();
    }

    private void render(double alpha) {
        // Render-only operations
        asteroidPool.addAsteroidsToPane();
    }


    /**
     * Initializes the game window scene.
     */
    @FXML
    public void initialize(Pane gamePane) {
        // Enable VSync optimization for fixed window size
        System.setProperty("javafx.animation.fullspeed", "false");
        System.setProperty("javafx.animation.pulse", String.valueOf((int)(1000.0 / REFRESH_RATE)));
        System.setProperty("prism.vsync", "true");

        this.player = new Player(gamePane);
        this.levelController = new LevelController();
        this.asteroidPool = new AsteroidPool(this.gamePane, levelController.getEnemyType1(), levelController.getEnemyType2(), levelController.getEnemyType3());

        // Initialise player controls
        PlayerControls playerControls = new PlayerControls(player, this.gamePane, this.getSoundPool(), this);

        asteroidsThatHitShip = new HashMap<>();
        player.setLives();

        setGameLabels();
        setupButtons();

        // Restart pause flag
        gameIsPaused = false;

        gameLoop.start();
    }

    /**
     * Gets the game pane.
     *
     * @return The game pane.
     */
    public Pane getGamePane() {
        return this.gamePane;
    }

    /**
     * Sets up the game labels (level, score, pause, and game over).
     */
    private void setGameLabels() {
        levelLabel = new Label("Level: " + levelController.getLevel());
        levelLabel.getStyleClass().add("display-label");
        levelLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/labels.css")).toExternalForm());
        levelLabel.setPrefWidth(150);
        levelLabel.setPrefHeight(60);
        levelLabel.setLayoutX(WINDOW_WIDTH/2 - levelLabel.getPrefWidth() / 2);

        scoreLabel = new Label(String.format("Score: %07d", player.getScore()));
        scoreLabel.getStyleClass().add("display-label");
        scoreLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/labels.css")).toExternalForm());
        scoreLabel.setPrefWidth(240);
        scoreLabel.setPrefHeight(60);
        scoreLabel.setLayoutX(WINDOW_WIDTH - scoreLabel.getPrefWidth());

        pauseLabel = new Label("Paused");
        pauseLabel.getStyleClass().add("title-label");
        pauseLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/labels.css")).toExternalForm());
        pauseLabel.setPrefWidth(300);
        pauseLabel.setPrefHeight(60);
        pauseLabel.setLayoutX(WINDOW_WIDTH/2 - pauseLabel.getPrefWidth() / 2);
        pauseLabel.setLayoutY(200);

        gameOverLabel = new Label("Game Over");
        gameOverLabel.getStyleClass().add("title-label");
        gameOverLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/labels.css")).toExternalForm());
        gameOverLabel.setPrefWidth(WINDOW_WIDTH/2);
        gameOverLabel.setLayoutX(WINDOW_WIDTH/2 - gameOverLabel.getPrefWidth() / 2);
        gameOverLabel.setLayoutY(200);

        fpsCounter = new Label("FPS: --");
        fpsCounter.getStyleClass().add("FPS-label");
        fpsCounter.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/labels.css")).toExternalForm());
        fpsCounter.setPrefWidth(120);
        fpsCounter.setPrefHeight(30);
        fpsCounter.setLayoutX(WINDOW_WIDTH - fpsCounter.getPrefWidth());
        fpsCounter.setLayoutY(WINDOW_HEIGHT - fpsCounter.getPrefHeight());

        gamePane.getChildren().addAll(levelLabel, scoreLabel, fpsCounter);
    }

    /**
     * Sets up the back and restart buttons.
     */
    private void setupButtons() {
        backButton = new Button("Menu");
        setUpForGamePane(backButton);
        backButton.setLayoutY(450 - backButton.getPrefHeight());
        backButton.setOnAction(event -> {
            try {
                switchToMainMenu(event);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            }
        });

        restartButton = new Button("Restart");
        setUpForGamePane(restartButton);
        restartButton.setLayoutY(450 - restartButton.getPrefHeight() - backButton.getPrefHeight());
        restartButton.setOnAction(event -> {
            try {
                switchToGameWindow(event);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            }

        });
    }

    /**
     * Sets up common properties for game buttons.
     *
     * @param button The button to set up.
     */
    private void setUpForGamePane(Button button) {
        button.setDefaultButton(false);
        button.setFocusTraversable(false);
        button.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/buttons.css")).toExternalForm());
        button.setPrefWidth(400);
        button.setPrefHeight(50);
        button.setLayoutX(WINDOW_WIDTH/2 - button.getPrefWidth() / 2);
    }


    /**
     * Updates the player's score after an asteroid is destroyed.
     *
     * @param asteroid The destroyed asteroid.
     */
    private void updateScore(Asteroid asteroid) {
        player.increaseScore(asteroid.getScore());
        scoreLabel.setText(String.format("Score: %07d", player.getScore()));
    }

    /**
     * Makes the entity bounds visible for debugging purposes.
     */
    public static void makeBoundsVisible() {
        Entity.visibleBounds = true;
    }

    /**
     * Makes the entity bounds invisible.
     */
    public static void makeBoundsInvisible() {
        Entity.visibleBounds = false;
    }

    /**
     * Pauses the game.
     */
    public void pauseGame() {
        gameIsPaused = true;
        getSoundPool().playPauseClip();
        gamePane.getChildren().addAll(pauseLabel, backButton, restartButton);
        this.gameLoop.stop();
    }

    /**
     * Resumes the game.
     */
    public void resumeGame() {
        gameIsPaused = false;
        getSoundPool().playUnpauseClip();
        gamePane.getChildren().removeAll(pauseLabel, backButton, restartButton);
        this.gameLoop.start();
    }


    /**
     * Handles game over conditions.
     */
    private void gameOver() {
        if (player.getLives() <= 0) {

            getSoundPool().stopBackgroundMusic();
            getSoundPool().playGameOverClip();
            // Save player score
            scoreManager.addScore(Player.name, player.getScore(), String.valueOf(levelController.getLevel()));

            // Sometimes all three nodes are already in the pane when this is called?
            if (!gamePane.getChildren().contains(gameOverLabel)) {
                gamePane.getChildren().add(gameOverLabel);
            }
            if(!gamePane.getChildren().contains(backButton)) {
                gamePane.getChildren().add(backButton);
            }
            if (!gamePane.getChildren().contains(restartButton)) {
                gamePane.getChildren().add(restartButton);
            }
            gameLoop.stop();
        }
    }

    /**
     * Update level modifiers, create a new asteroid pool with new modifiers, updates level label, and plays the level up sound.
     */
    private void levelUp() {
        if (asteroidPool.getActiveAsteroids().isEmpty() && asteroidPool.getPool().isEmpty()) {

            levelController.levelUp();
            // Create new asteroid pool with new asteroid attributes
            asteroidPool = new AsteroidPool(this.gamePane, levelController.getEnemyType1(), levelController.getEnemyType2(), levelController.getEnemyType3());
            // Update level
            levelLabel.setText("Level: " + levelController.getLevel()); // Update shown level
        }
    }

    // ================================================================================================
    //                                    Collision detection
    // ================================================================================================

    private void checkCollisions() {
        Spaceship spaceship = player.getSpaceship();

        // Create a list of asteroids to be removed
        ArrayList<Asteroid> asteroidToRemove = new ArrayList<>();
        ArrayList<Laser> lasersToRemove = new ArrayList<>();

        for (Asteroid asteroid : asteroidPool.getActiveAsteroids()) {
            if (asteroid != null && gamePane.getChildren().contains(asteroid.getView()) && !asteroidToRemove.contains(asteroid)) {
                for (Laser laser : spaceship.getLaserPool().getActiveLasers()) {
                    if (laser != null && gamePane.getChildren().contains(laser.getView())) {
                        if (CollisionDetector.didCirclesCollide((Circle) asteroid.getBounds(), (Circle) laser.getBounds())) {
                            asteroid.removeHitPoint();
                            asteroid.changeView(gamePane); // Change the view of the asteroid depending on current hit points
                            if (asteroid.getHitPoints() == 0) {
                                this.getSoundPool().playDestructionClip();
                                asteroidToRemove.add(asteroid);
                                gamePane.getChildren().removeAll(asteroid.getBounds(), asteroid.getView());
                            }
                            laser.resetDistanceTraveled();
                            gamePane.getChildren().removeAll(laser.getBounds(), laser.getView());
                            lasersToRemove.add(laser);
                            spaceship.getLaserPool().getInactiveLasers().add(laser);
                            updateScore(asteroid);
                            break;
                        }
                    }
                }
                double collisionTimeElapsed;
                if (CollisionDetector.didShipCollide(spaceship.getBounds(), asteroid.getBounds())) {
                    // Case 1: there was a collision, and it is the first time within the last second that this asteroid hit the ship
                    if (!asteroidsThatHitShip.containsKey(asteroid.getId())) {
                        this.getSoundPool().playCollisionClip();
                        asteroidsThatHitShip.put(asteroid.getId(), (double) System.nanoTime());
                        player.removeLife();
                    } else {
                        collisionTimeElapsed = System.nanoTime() - asteroidsThatHitShip.get(asteroid.getId());
                        // Case 2: there was another collision with the same asteroid after 1 second
                        if (collisionTimeElapsed > NANOS_PER_SECOND) {
                            this.getSoundPool().playCollisionClip();
                            asteroidsThatHitShip.put(asteroid.getId(), (double) System.nanoTime());
                            player.removeLife();
                        }
                    }
                } else {
                    // Case 3: There was no collision with the asteroid after 1 second
                    if (asteroidsThatHitShip.containsKey(asteroid.getId())) {
                        collisionTimeElapsed = System.nanoTime() - asteroidsThatHitShip.get(asteroid.getId());
                        if (collisionTimeElapsed > NANOS_PER_SECOND) {
                            asteroidsThatHitShip.remove(asteroid.getId());
                        }
                    }
                }
            }
        }
        // Cleanup code
        if (!asteroidToRemove.isEmpty()) {
            asteroidPool.getActiveAsteroids().removeAll(asteroidToRemove);
            asteroidToRemove.clear();
        }
        if (!lasersToRemove.isEmpty()) {
            spaceship.getLaserPool().getActiveLasers().removeAll(lasersToRemove);
            lasersToRemove.clear();
        }
    }

    /**
     * Clean resources for game closure.
     */
    public void cleanup() {
        // Stop the game loop
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }

        // Remove level controller
        if (levelController != null) {
            levelController = null;
        }

        // Clear all game objects
        if (gamePane != null) {
            gamePane.getChildren().clear();
        }

        // Clean up spaceship resources
        if (player != null) {
            player.cleanup();
            player = null;
        }

        // Clean up asteroid pool
        if (asteroidPool != null) {
            asteroidPool.cleanup();
            asteroidPool = null;
        }

        // Clear collections
        if (asteroidsThatHitShip != null) {
            asteroidsThatHitShip.clear();
            asteroidsThatHitShip = null;
        }

        System.out.println("GameController cleanup complete");
    }
}

package org.example.asteroidsrevamped;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class Spaceship extends Entity {

    private final Pane gamePane;

    // Spaceship parameters:
    private ImageView spaceshipView;
    private double velocityX = 0;
    private double velocityY = 0;
    private final static double MAX_SPEED = 150;
    private final static double ACCELERATION = 75;
    private final static double ROTATION_SPEED = 80;

    // Movement parameters
    private boolean rotateRight = false;
    private boolean rotateLeft = false;
    private boolean moveFront = false;
    private boolean moveBack = false;

    // Laser creation and pooling
    private final LaserPool laserPool = new LaserPool();

    // Visual representation of ammunition
    private ProgressBar ammunition = new ProgressBar();

    public Spaceship(Pane gamePane) {

        this.gamePane = gamePane;

        // Progress bar (ammunition)
        ammunition.setProgress(1);
        ammunition.setRotate(-90);
        ammunition.setTranslateY(GameController.WINDOW_HEIGHT - 100);
        ammunition.getStyleClass().add("ammunition");
        ammunition.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/ammunition.css")).toExternalForm());

        // Spaceship Image
        Image spaceshipImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/spaceship.png")));
        spaceshipView = new ImageView(spaceshipImage);
        spaceshipView.setFitHeight(60);
        spaceshipView.setFitWidth(60);
        spaceshipView.setLayoutX(GameController.WINDOW_WIDTH/2 - spaceshipView.getFitWidth()/2);
        spaceshipView.setLayoutY(GameController.WINDOW_HEIGHT/2 - spaceshipView.getFitHeight());

        this.bounds = new SpaceshipShape().getShape();
        bounds.setLayoutX(GameController.WINDOW_WIDTH/2 - spaceshipView.getFitWidth()/2);
        bounds.setLayoutY(GameController.WINDOW_HEIGHT/2 - spaceshipView.getFitHeight());
        this.bounds.setVisible(false);

        gamePane.getChildren().addAll(spaceshipView, ammunition);
    }


    public void updatePosition(double deltaTime) {
        // Update the spaceshipView's position based on velocity
        double xPos = spaceshipView.getLayoutX() + velocityX * deltaTime;
        double yPos = spaceshipView.getLayoutY() + velocityY * deltaTime;

        // Wrap position if out of bounds

        if (xPos < -spaceshipView.getFitWidth() / 2) {
            xPos = gamePane.getWidth();
        } else if (xPos > gamePane.getWidth()) {
            xPos = -spaceshipView.getFitWidth() / 2;
        }

        if (yPos < -spaceshipView.getFitHeight() / 2) {
            yPos = gamePane.getHeight();
        } else if (yPos > gamePane.getHeight()) {
            yPos = -spaceshipView.getFitHeight() / 2;
        }

        // Update spaceship and bounds position
        spaceshipView.setLayoutX(xPos);
        this.bounds.setLayoutX(xPos);
        spaceshipView.setLayoutY(yPos);
        this.bounds.setLayoutY(yPos);

        // Scale rotation speed with deltaTime
        double rotationAmount = ROTATION_SPEED * deltaTime;

        // Allow movement if dedicated key is pressed down
        if (rotateLeft) {
            spaceshipView.setRotate(spaceshipView.getRotate() - rotationAmount);
            this.bounds.setRotate(spaceshipView.getRotate() + rotationAmount);
        }

        if (rotateRight) {
            spaceshipView.setRotate(spaceshipView.getRotate() + rotationAmount);
            this.bounds.setRotate(spaceshipView.getRotate() - rotationAmount);
        }

        if (moveFront) {
            frontMovementCalc(deltaTime);
        }

        if (moveBack) {
            backMovementCalc(deltaTime);
        }

        // Toggle bounds
        if (Entity.visibleBounds && !gamePane.getChildren().contains(this.getBounds())) {
            this.makeBoundsVisible(gamePane);
        } else {
            this.makeBoundsInvisible(gamePane);
        }
    }

    public void moveFront() {
        moveFront = true;
    }

    public void notMoveFront() {
        moveFront = false;
    }

    public void rotateLeft() {
        rotateLeft = true;
    }

    public void rotateRight() {
        rotateRight = true;
    }

    public void stopRotating() {
        rotateLeft = false;
        rotateRight = false;
    }

    public void moveBack() {
        moveBack = true;
    }

    public void notMoveBack() {
        moveBack = false;
    }

    private void frontMovementCalc(double deltaTime) {
        // Calculate the angle in radians from the current rotation
        double angleInRadians = Math.toRadians(spaceshipView.getRotate());

        // Apply ACCELERATION to the velocity components
        velocityX += Math.cos(angleInRadians) * ACCELERATION * deltaTime;
        velocityY += Math.sin(angleInRadians) * ACCELERATION * deltaTime;

        // Cap the velocity to prevent it from exceeding MAX_SPEED
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (speed > MAX_SPEED) {
            velocityX = (velocityX / speed) * MAX_SPEED;
            velocityY = (velocityY / speed) * MAX_SPEED;
        }
    }

    private void backMovementCalc(double deltaTime) {
        // Deceleration factor
        double decelerationFactor = 0.5; // Smaller value = faster deceleration. From 0 to 1.

        // Reduce velocity components proportionally
        velocityX *= Math.pow(decelerationFactor, deltaTime);
        velocityY *= Math.pow(decelerationFactor, deltaTime);

        // Optional: Stop movement if speed is below a certain threshold
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (speed < 10) { // Threshold for minimal movement
            velocityX = 0;
            velocityY = 0;
        }
    }

    public Laser shoot() {
        Laser laser = laserPool.getLaser();
        if (laser != null) {
            ImageView laserView = laser.getView();

            // Calculate center of spaceshipView
            double spaceshipCenterX = spaceshipView.getLayoutX() + spaceshipView.getFitWidth() / 2;
            double spaceshipCenterY = spaceshipView.getLayoutY() + spaceshipView.getFitHeight() / 2;

            // Calculate offset from center where laser should appear (front of spaceshipView)
            double angleInRadians = Math.toRadians(spaceshipView.getRotate());
            double offsetDistance = (spaceshipView.getFitWidth() / 2) + 10; // Distance from center to front of spaceshipView

            // Calculate laser starting position. for the center of laser view to align with the center of the spaceship, we deduct half the laser height from it
            double laserStartX = spaceshipCenterX + (Math.cos(angleInRadians) * offsetDistance) - laserView.getFitWidth()/2;
            double laserStartY = spaceshipCenterY + (Math.sin(angleInRadians) * offsetDistance);
            // Calculate laser bounds starting position
            double laserBoundsStartX = spaceshipCenterX + (Math.cos(angleInRadians) * (offsetDistance + laserView.getFitWidth()-laserView.getFitWidth()/1.5));
            double laserBoundsStartY = spaceshipCenterY + (Math.sin(angleInRadians) * (offsetDistance + laserView.getFitWidth()-laserView.getFitWidth()/1.5));

            // Set laser rotation and starting position
            laser.setRotation(spaceshipView.getRotate());
            laser.setPosition(laserStartX, laserStartY, laserBoundsStartX, laserBoundsStartY);

            return laser;
        }
        return null;
    }


    public LaserPool getLaserPool() {
        return laserPool;
    }

    public ProgressBar getAmmunition() {
        return ammunition;
    }

    public void cleanup() {
        // Remove spaceshipView off game pane
        gamePane.getChildren().remove(spaceshipView);

        // Remove any active lasers off game pane
        for (Laser laser : laserPool.getActiveLasers()) {
            gamePane.getChildren().remove(laser.getView());
        }

        // Clear laser pool
        laserPool.cleanup();

        // Delete spaceshipView view
        spaceshipView = null;

        // Delete ammunition progress bar
        ammunition = null;

        System.out.println("Spaceship cleanup complete");

    }

}
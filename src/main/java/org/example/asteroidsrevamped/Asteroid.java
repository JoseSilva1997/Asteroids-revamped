package org.example.asteroidsrevamped;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Parent class of Asteroid1,2, and 3. Holds the direction and rotation of Asteroid enemy type.
 *
 * @author Jose Silva
 */
public abstract class Asteroid extends Enemy {

    private final double velocityX;
    private final double velocityY;
    private final double rotationSpeed;

    protected Asteroid(ImageView view, int size, String name, double speed, int boundAdjustment, int hitPoints, int score) {
        super(size, view, name, hitPoints, score);
        // Define ranges for angles, avoiding near-horizontal or near-vertical trajectories
        double[] angleRanges = {
                ThreadLocalRandom.current().nextDouble(15, 75),
                ThreadLocalRandom.current().nextDouble(105, 165),
                ThreadLocalRandom.current().nextDouble(195, 255),
                ThreadLocalRandom.current().nextDouble(285, 345)
        };
        // Randomly pick an angle from one of the ranges
        double angle = angleRanges[ThreadLocalRandom.current().nextInt(angleRanges.length)];
        angle = Math.toRadians(angle);

        this.velocityX = Math.cos(angle) * speed;
        this.velocityY = Math.sin(angle) * speed;
        this.rotationSpeed = 20. / this.getSize();

        // Create a circle to work as the bounds of an asteroid.
        this.bounds = new Circle(view.getX(), view.getY(), (float) size/2 - boundAdjustment);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    /**
     * Updates the position of the asteroid and its bounds at the same time.
     * @param x is the asteroid's x position.
     * @param y is the asteroid's y position.
     */
    public void setPosition (double x, double y) {
        this.view.setX(x);
        this.view.setY(y);
        this.bounds.setLayoutX(view.getX() + (float)this.getSize()/2);
        this.bounds.setLayoutY(view.getY() + (float)this.getSize()/2);
    }
}
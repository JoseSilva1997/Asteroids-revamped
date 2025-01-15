package org.example.asteroidsrevamped;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * This class represents a laser fired by the player's spaceship.
 *
 * @author Jose Silva
 */
public class Laser extends Entity {

    private static final Image LASER_IMAGE = new Image(Objects.requireNonNull(Laser.class.getResourceAsStream("/images/laser.png")));
    private static final int LASER_WIDTH = 20;
    private static final int LASER_HEIGHT = 10;
    private static final int LASER_SPEED = 2500;
    private double boundX;
    private double boundY;
    private double initialRotation;
    private double distanceTraveled = 0;

    public Laser() {
        this.view = new ImageView(LASER_IMAGE);
        this.view.setFitWidth(LASER_WIDTH);
        this.view.setFitHeight(LASER_HEIGHT);
        this.bounds = new Circle(view.getX(),view.getY(),(float)LASER_HEIGHT/3);
    }

    /**
     * Gets the rotation of the laser.
     *
     * @return The laser's rotation.
     */
    public double getRotation() {
        return initialRotation;
    }

    /**
     * Gets the distance traveled by the laser.
     *
     * @return The distance traveled by the laser.
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Gets the x-coordinate of the laser's bounds.
     *
     * @return The x-coordinate of the bounds.
     */
    public double getBoundX() {
        return boundX;
    }

    /**
     * Gets the y-coordinate of the laser's bounds.
     *
     * @return The y-coordinate of the bounds.
     */
    public double getBoundY() {
        return boundY;
    }

    /**
     * Adds a distance traveled to the laser's total distance traveled.
     *
     * @param distanceTraveled The distance to add.
     */
    public void addDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled += distanceTraveled;
    }

    /**
     * Resets the distance traveled by the laser to 0.
     */
    public void resetDistanceTraveled() {
        distanceTraveled = 0;
    }

    /**
     * Sets the rotation of the laser.
     *
     * @param rotation The new rotation of the laser.
     */
    public void setRotation(double rotation) {
        initialRotation = rotation;
        view.setRotate(rotation);
    }

    public int getLaserSpeed() {
        return LASER_SPEED;
    }

    /**
     * Sets the position of the laser and its bounding circle.
     *
     * @param x The x-coordinate of the laser's position.
     * @param y The y-coordinate of the laser's position.
     * @param laserX The x-coordinate of the laser's center within the spaceship.
     * @param laserY The y-coordinate of the laser's center within the spaceship.
     */
    public void setPosition(double x, double y, double laserX, double laserY) {
        view.setX(x);
        view.setY(y);
        boundX = laserX;
        boundY = laserY;
        bounds.setLayoutX(laserX);
        bounds.setLayoutY(laserY+ (float)LASER_HEIGHT/2);
    }
}

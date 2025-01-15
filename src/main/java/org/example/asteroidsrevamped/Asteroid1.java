package org.example.asteroidsrevamped;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Asteroid of easy difficulty. Slow speed, average size, 1 hit-point.
 *
 * @author Jose Silva
 */
public class Asteroid1 extends Asteroid{

    // Asteroid attributes
    private static final Image ASTEROID_IMAGE = new Image(Objects.requireNonNull(Asteroid.class.getResourceAsStream("/images/asteroid1.png")));
    private static final String NAME = "easy_asteroid";
    private static final int SIZE = 60;
    private static final double SPEED = 100;
    private static final int SCORE = 25;
    private static final int HIT_POINTS = 1;
    // Adjusts the bounds of the asteroid. Different asteroid PNGs require different bounds.
    private static final int BOUND_ADJUSTMENT = 5;


    Asteroid1(){
        ImageView asteroidImageView = new ImageView(ASTEROID_IMAGE);
        asteroidImageView.setFitWidth(SIZE);
        asteroidImageView.setFitHeight(SIZE);
        super(asteroidImageView,SIZE, NAME, SPEED, BOUND_ADJUSTMENT,HIT_POINTS, SCORE);
    }
}

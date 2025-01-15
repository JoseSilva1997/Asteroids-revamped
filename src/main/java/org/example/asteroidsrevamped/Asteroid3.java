package org.example.asteroidsrevamped;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Asteroid of hard difficulty. Small size, Fast movements, 1 hit point.
 *
 * @author Jose Silva
 */
public class Asteroid3 extends Asteroid {

    // Asteroid attributes
    private static final Image ASTEROID_IMAGE = new Image(Objects.requireNonNull(Asteroid.class.getResourceAsStream("/images/asteroid2.png")));
    private static final String NAME = "small_and_fast";
    private static final int SIZE = 40;
    private static final double SPEED = 220;
    private static final int SCORE = 150;
    private static final int HIT_POINTS = 1;
    // Adjusts the bounds of the asteroid. Different asteroid PNGs require different bounds.
    private static final int BOUND_ADJUSTMENT = 2;


    Asteroid3(){
        ImageView asteroidImageView = new ImageView(ASTEROID_IMAGE);
        asteroidImageView.setFitWidth(SIZE);
        asteroidImageView.setFitHeight(SIZE);
        super(asteroidImageView,SIZE, NAME, SPEED, BOUND_ADJUSTMENT, HIT_POINTS, SCORE);
    }
}

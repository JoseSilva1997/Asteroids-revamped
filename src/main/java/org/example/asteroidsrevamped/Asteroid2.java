package org.example.asteroidsrevamped;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Asteroid of medium difficulty. Large size, slow movement, 2 hit-points.
 *
 * @author Jose Silva
 */
public class Asteroid2 extends Asteroid {

    // Asteroid attributes
    private static final Image ASTEROID_IMAGE = new Image(Objects.requireNonNull(Asteroid.class.getResourceAsStream("/images/asteroid3.png")));
    private static final Image ASTEROID_IMAGE_BROKEN = new Image(Objects.requireNonNull(Asteroid.class.getResourceAsStream("/images/asteroid3-broken.png")));
    private static final String NAME = "big_asteroid";
    private static final int SIZE = 120;
    private static final double SPEED = 120;
    private static final int SCORE = 50;
    private static final int HIT_POINTS = 2;
    // Adjusts the bounds of the asteroid. Different asteroid PNGs require different bounds.
    private static final int BOUND_ADJUSTMENT = 8;


    Asteroid2() {
        ImageView asteroidImageView = new ImageView(ASTEROID_IMAGE);
        asteroidImageView.setFitWidth(SIZE);
        asteroidImageView.setFitHeight(SIZE);
        super(asteroidImageView,SIZE, NAME, SPEED, BOUND_ADJUSTMENT, HIT_POINTS, SCORE);
    }

    /**
     * Change the asteroid's ImageView to that of a broken asteroid.
     * @param gamePane reference to the pane where asteroidImageView is located.
     */
    public void changeView(Pane gamePane) {
        if(getHitPoints() < 2){
            this.view.setImage(ASTEROID_IMAGE_BROKEN);
        }
    }

}

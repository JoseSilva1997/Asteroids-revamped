package org.example.asteroidsrevamped;

import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Store collision detection methods.
 *
 * @author Jose Silva.
 */
public class CollisionDetector {

    /**
     * Calculates de distance between the centers of 2 circle shapes.
     * Returns true if the distance calculated is less than the sum of their radii.
     * @param circle1 first circle shape.
     * @param circle2 second circle shape.
     * @return boolean
     */
    public static boolean didCirclesCollide(Circle circle1, Circle circle2) {

        double dx = circle1.getLayoutX() - circle2.getLayoutX();
        double dy = circle1.getLayoutY() - circle2.getLayoutY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= (circle1.getRadius() + circle2.getRadius());
    }

    /**
     * Performs shape intersection between 2 shapes.
     * Return true if the width of the intersection of 2 shapes is not equal to -1 (shapes intersect).
     * @param spaceship spaceship shape.
     * @param shape second shape.
     * @return boolean
     */
    public static boolean didShipCollide(Shape spaceship, Shape shape) {
        Bounds bounds1 = spaceship.getBoundsInLocal();
        Bounds bounds2 = shape.getBoundsInLocal();

        if (!bounds1.intersects(bounds2)) {
            return false;
        }
        Shape intersection = Shape.intersect(spaceship, shape);
        return intersection.getBoundsInLocal().getWidth() != -1;
    }
}
package org.example.asteroidsrevamped;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Represents the graphical shape of a spaceship by combining various geometric components.

 * This class encapsulates the creation of the spaceship shape and provides a method to
 * retrieve the composite shape.
 *
 * @author Jose Silva
 */
 public class SpaceshipShape {

    /** The composite shape representing the spaceship. */
    private final Shape shape;

    /**
     * Constructs a new SpaceshipShape by combining predefined geometric shapes.

     * The shape includes a left wing, a body, a right wing, and a nose, all combined
     * into a single {@link Shape} object.
     */
    SpaceshipShape(){
        // Create the left wing of the spaceship.
        Polygon leftWing = new Polygon();
        leftWing.getPoints().addAll(7.0, 2.0, 7.0, 20.0, 40.0, 20.0);

        // Create the rectangular body of the spaceship.
        Rectangle body = new Rectangle();
        body.setX(0.0);
        body.setY(20.0);
        body.setWidth(43);
        body.setHeight(20);

        // Combine the left wing and the body into a single shape.
        Shape shape1 = Shape.union(leftWing, body);

        // Create the right wing of the spaceship.
        Polygon rightWing = new Polygon();
        rightWing.getPoints().addAll(7.0, 40.0, 7.0, 58.0, 40.0, 40.0);

        // Create the elliptical nose of the spaceship.
        Ellipse nose = new Ellipse();
        nose.setCenterX(43);
        nose.setCenterY(30.0);
        nose.setRadiusX(17);
        nose.setRadiusY(10);

        // Combine the first composite shape with the nose.
        Shape shape2 = Shape.union(shape1, nose);

        // Combine the right wing with the rest of the shape to form the final composite shape.
        this.shape = Shape.union(rightWing, shape2);
    }

    /**
     * Returns the composite shape representing the spaceship.
     *
     * @return The spaceship's {@link Shape}.
     */
    public Shape getShape(){
        return this.shape;
    }

}

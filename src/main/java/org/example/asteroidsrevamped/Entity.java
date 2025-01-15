package org.example.asteroidsrevamped;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Abstract class representing an entity in the game world.

 * This class provides a foundation for all entities in the game, including enemies, and the player.
 * It defines common properties like bounds (collision detection) and view (visual representation).
 *
 * @author Jose Silva
 */
public abstract class Entity {

    /**
     * The shape representing the entity's collision bounds.
     */
    protected Shape bounds;

    /**
     * The ImageView representing the entity's visual appearance.
     */
    protected ImageView view;

    /**
     * Static flag indicating whether entity bounds should be visible for debugging purposes.
     */
    public static boolean visibleBounds;

    Entity() {
    }

    /**
     * Gets the shape representing the entity's collision bounds.
     *
     * @return The shape representing the entity's bounds.
     */
    public Shape getBounds() {
        return bounds;
    }

    /**
     * Gets the ImageView representing the entity's visual appearance.
     *
     * @return The ImageView representing the entity's view.
     */
    public ImageView getView() {
        return view;
    }

    /**
     * Makes the entity's collision bounds visible on the pane.
     * This method adds the bounds shape to the pane with a red stroke for debugging purposes.
     * @param pane The pane where the entity is located.
     */
    public void makeBoundsVisible(Pane pane) {
        bounds.setVisible(true);
        bounds.setFill(Color.TRANSPARENT);
        bounds.setStroke(Color.RED);
        bounds.setStrokeWidth(2);
        pane.getChildren().add(bounds);
    }

    /**
     * Makes the entity's collision bounds invisible on the pane.
     * This method removes the bounds shape from the pane.

     * @param pane The pane where the entity is located.
     */
    public void makeBoundsInvisible(Pane pane) {
        bounds.setVisible(false);
        pane.getChildren().remove(bounds);
    }
}

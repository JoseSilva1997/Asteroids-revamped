package org.example.asteroidsrevamped;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Abstract class representing an enemy in the game world.
 
 * This class provides the foundation to all enemies in the game world.
 * @author Jose Silva
 */
public abstract class Enemy extends Entity {

    private final int size;
    protected String name;
    private static int nextID = 0;
    private final int id;
    private final int score;
    private int hitPoints;

    /**
     * Enemy constructor that takes the size, view, name, hit points, and score of the enemy.
     *
     * @param size The size of the enemy.
     * @param view The ImageView representing the enemy.
     * @param name The name of the enemy.
     * @param hitPoints The initial number of hit points the enemy has.
     * @param score The score awarded for defeating the enemy.
     */
    Enemy (int size, ImageView view, String name, int hitPoints, int score) {
        this.size = size;
        this.view = view;
        this.name = name;
        this.hitPoints = hitPoints;
        this.score = score;
        nextID++;
        id = nextID;
    }

    // Getters
    /**
     * Gets the score awarded for defeating the enemy.
     *
     * @return The score of the enemy.
     */
    public int getScore () {
        return score;
    }

    /**
     * Gets the unique ID of the enemy.
     *
     * @return The ID of the enemy.
     */
    public int getId () {
        return id;
    }

    /**
     * Gets the current number of hit points of the enemy.
     *
     * @return The number of hit points of the enemy.
     */
    public int getHitPoints () {
        return hitPoints;
    }

    /**
     * Gets the size of the enemy.
     *
     * @return The size of the enemy.
     */
    public int getSize () {
        return size;
    }


    /**
     * Removes one hit point from the enemy.
     */
    public void removeHitPoint () {
        if (hitPoints >= 0) {
            hitPoints--;
        }
    }

    /**
     * Changes the view of the enemy depending on its state.

     * This method should be implemented by subclasses to update the enemy's view
     * based on its current state (e.g., alive, damaged, destroyed).

     * @param pane The pane where the enemy's view is located.
     */
    public void changeView(Pane pane) {

    }

}

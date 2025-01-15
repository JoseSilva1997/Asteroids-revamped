package org.example.asteroidsrevamped;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Stores an amount of asteroids to be used per level. Requires passing it how many enemies of each type to create.
 * This pool is supposed to be updated each level.
 *
 * @author Jose Silva
 */
public class AsteroidPool {

    private static final double SECOND_PER_FRAMES = 0.016;
    private static final double NANOS_PER_SECOND = 1000000000;

    private final Pane gamePane;

    // randomise pool
    private final Set<Asteroid> randomAsteroids = new HashSet<>();
    // pool is used to store available asteroids
    private final Deque<Asteroid> pool = new ArrayDeque<>();
    // pool of asteroids currently in the gamePane
    private final Deque<Asteroid> activeAsteroids = new ArrayDeque<>();

    private double previousSpawnTime;

    private final int enemyType1;
    private final int enemyType2;
    private final int enemyType3;

    public AsteroidPool(Pane gamePane, int enemyType1, int enemyType2, int enemyType3) {
        this.gamePane = gamePane;
        this.enemyType1 = enemyType1;
        this.enemyType2 = enemyType2;
        this.enemyType3 = enemyType3;
        createAsteroids();
    }

    /**
     * returns the next asteroid in the queue. Returns null if pool is empty.
     * @return Asteroid
     */
    public Asteroid getAsteroid() {
        if (!pool.isEmpty()) {
            Asteroid asteroid = pool.poll();
            activeAsteroids.offer(asteroid);
            return asteroid;
        }
        return null;
    }

    public Deque<Asteroid> getPool() {
        return pool;
    }

    public Deque<Asteroid> getActiveAsteroids() {
        return activeAsteroids;
    }

    /**
     * Creates enemies based on parameters passed to the AsteroidPool constructor and adds them to the randomAsteroids set to be randomised.
     * Then, adds them to the pool queue for easy removal and disposal.
     */
    private void createAsteroids() {
        if (enemyType1 != 0) {
            for (int i = 0; i < enemyType1; i++) {
                Asteroid asteroid = new Asteroid1();
                randomAsteroids.add(asteroid);
            }
        }
        if (enemyType2 != 0) {
            for (int i = 0; i < enemyType2; i++) {
                Asteroid asteroid = new Asteroid2();
                randomAsteroids.add(asteroid);
            }
        }
        if (enemyType3 != 0) {
            for (int i = 0; i < enemyType3; i++) {
                Asteroid asteroid = new Asteroid3();
                randomAsteroids.add(asteroid);
            }
        }
        for (Asteroid asteroid : randomAsteroids) {
            pool.offer(asteroid);
        }
        randomAsteroids.clear();
    }

    /**
     * Remove an asteroid from the pool every second, and adds them to the game pane.
     */
    public void addAsteroidsToPane() {
        if (!pool.isEmpty()) {
            // Time between each asteroid spawn
            double spawnTimeElapsed = System.nanoTime() - previousSpawnTime;
            if (spawnTimeElapsed > NANOS_PER_SECOND) { // Create a new asteroid every second
                Asteroid asteroid = getAsteroid();
                // Set asteroid to random position
                asteroid.setPosition(ThreadLocalRandom.current().nextDouble(-asteroid.getView().getFitWidth(),
                        gamePane.getWidth() + asteroid.getView().getFitWidth()), -asteroid.getView().getFitHeight());
                gamePane.getChildren().add(asteroid.getView());
                previousSpawnTime = System.nanoTime();
            }
        }
    }

    /**
     * Calculate and update each active asteroid's position on a pane
     */
    public void updateAsteroids(double deltaTime) {
        for (Asteroid asteroid : getActiveAsteroids()) {
            ImageView asteroidView = asteroid.getView();
            asteroid.setPosition((asteroidView.getX() + asteroid.getVelocityX() * deltaTime), asteroidView.getY() + asteroid.getVelocityY() * deltaTime);
            // Toggle bounds
            if (Entity.visibleBounds && !gamePane.getChildren().contains(asteroid.getBounds())) {
                asteroid.makeBoundsVisible(gamePane);
            } else {
                asteroid.makeBoundsInvisible(gamePane);
            }
            asteroidView.setRotate(asteroidView.getRotate() + asteroid.getRotationSpeed());
            wrapAround(asteroidView);
        }
    }

    /**
     * Wraps around asteroid position when reaching a border.
     * @param node is the asteroid in question.
     */
    private void wrapAround(ImageView node) {
        if (node.getX() > gamePane.getWidth()) {
            node.setX(-node.getFitWidth());
        } else if (node.getX() < -node.getFitWidth()) {
            node.setX(gamePane.getWidth());
        }
        if (node.getY() > gamePane.getHeight()) {
            node.setY(-node.getFitHeight());
        } else if (node.getY() < -node.getFitHeight()) {
            node.setY(gamePane.getHeight());
        }
    }

    /**
     * Cleanup resources.
     */
    public void cleanup() {
        randomAsteroids.clear();
        activeAsteroids.clear();
        pool.clear();

        System.out.println("AsteroidPool cleanup complete");
    }
}

package org.example.asteroidsrevamped;

import javafx.scene.image.ImageView;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class manages a pool of Laser objects for efficient reuse.
 *
 * @author Jose Silva
 */
public class LaserPool {

    private static final int NANOS_PER_SECOND = 1000000000;
    private static final int MAX_POOL_SIZE = 10;
    // used to store available lasers
    private final Deque<Laser> pool = new ArrayDeque<>(MAX_POOL_SIZE);
    // used to store lasers that are currently in the pane
    private final Deque<Laser> activeLasers = new ArrayDeque<>();
    // used to store laser that have been removed from the pane and waiting to be reloaded
    private final Deque<Laser> inactiveLasers = new ArrayDeque<>();

    private double previousReloadTime;

    /**
     * Constructs a new LaserPool and initializes it with a pool of lasers.
     */
    public LaserPool() {
        createLasers();
    }

    /**
     * Retrieves a laser from the pool.
     *
     * @return A laser object from the pool, or null if the pool is empty.
     */
    public Laser getLaser() {
        if (pool.isEmpty()) {
            return null;
        } else {
            Laser laser = pool.poll();
            activeLasers.offer(laser);
            resetLaser(laser);
            return laser;
        }
    }

    /**
     * Reloads a laser from the inactive queue to the pool.
     *
     * @return True if a laser was reloaded, false otherwise.
     */
    public boolean reloadLasers() {
        double elapsedTime = System.nanoTime() - previousReloadTime;
        if (!inactiveLasers.isEmpty() && elapsedTime > (float) NANOS_PER_SECOND/2) {
            Laser laser = inactiveLasers.poll();
            pool.offer(laser);
            previousReloadTime = System.nanoTime();
            return true;
        }
        return false;
    }

    /**
     * Creates a pool of lasers.
     */
    protected void createLasers() {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            Laser laser = new Laser();
            pool.offer(laser);
        }
    }

    /**
     * Resets the position and rotation of a laser.
     *
     * @param laser The laser to reset.
     */
    private void resetLaser(Laser laser) {
        ImageView laserView = laser.getView();
        laserView.setTranslateX(0);
        laserView.setTranslateY(0);
        laserView.setRotate(0);
    }

    /**
     * Gets the queue of active lasers.
     *
     * @return The queue of active lasers.
     */
    public Deque<Laser> getActiveLasers() {
        return activeLasers;
    }

    /**
     * Gets the queue of inactive lasers.
     *
     * @return The queue of inactive lasers.
     */
    public Deque<Laser> getInactiveLasers() {
        return inactiveLasers;
    }

    /**
     * Cleans up the laser pool by emptying all queues.
     */
    public void cleanup() {
        // Empty pool
        pool.clear();
        // Empty active lasers
        activeLasers.clear();
        // Empty inactive lasers
        inactiveLasers.clear();

        System.out.println("LaserPool cleanup complete");
    }
}
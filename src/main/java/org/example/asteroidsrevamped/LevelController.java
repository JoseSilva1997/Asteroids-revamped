package org.example.asteroidsrevamped;

/**
 * This class manages the difficulty and enemy distribution for each level.
 *
 * @author Jose Silva
 */
public class LevelController {

    /**
     * Current level.
     */
    private int level;

    /**
     * Total number of enemies in the pool.
     */
    private int poolSize;

    /**
     * Number of enemies of each type:
     * - enemyType1: Easy enemies
     * - enemyType2: Medium enemies
     * - enemyType3: Hard enemies
     */
    private int enemyType1;
    private int enemyType2;
    private int enemyType3;

    /**
     * Constructs a new LevelController, initializing it to level 1 with a default pool size.
     */
    public LevelController() {
        this.level = 1;
        this.poolSize = 10;
        calculateEnemyDistribution(level, poolSize);
    }

    /**
     * Gets the current level number.
     *
     * @return The current level number.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Calculates the distribution of enemy types based on the current level and pool size.
     *
     * @param level   The current level.
     * @param poolSize The total number of enemies in the pool.
     */
    private void calculateEnemyDistribution(int level, int poolSize) {
        double p1, p2; // Probabilities for easy and medium enemies

        if (level == 1) {
            // Force level 1 to be all easy enemies
            p1 = 1.0;
            p2 = 0.0;
        } else if (level <= 3) {
            // Levels 2-3: Only easy and medium enemies
            p1 = 1.0 - ((level - 1) * 0.15);  // Decrease easy enemies
            p2 = 1.0 - p1;                     // Rest are medium
        } else if (level <= 10) {
            // Levels 4-10: Introduce hard enemies
            p1 = Math.max(0, 0.7 - ((level - 3) * 0.15));  // Decrease easy faster
            p2 = Math.min(1.0, 0.3 + ((level - 3) * 0.1)); // Increase medium to 0.5

            // Normalize if sum is greater than 1.0
            double total = p1 + p2;
            if (total > 1.0) {
                p1 /= total;
                p2 /= total;
            }
        } else {
            // Level 11+: Gradual decrease in medium and increase in hard
            p1 = 0.0; // No easy enemies
            p2 = Math.max(0.5 - ((level - 11) * 0.05), 0.0); // Gradual decrease
        }

        // Calculate number of enemies for each type
        int e1 = (int) Math.round(p1 * poolSize);
        int e2 = (int) Math.round(p2 * poolSize);
        int e3 = poolSize - (e1 + e2); // Hard enemies are the remainder

        // Store the distribution
        enemyType1 = e1;
        enemyType2 = e2;
        enemyType3 = e3;

        // Debug output
        System.out.printf("Level %d - Easy: %.1f%% (%d), Medium: %.1f%% (%d), Hard: %.1f%% (%d)%n",
                level,
                ((double)e1 / poolSize) * 100, e1,
                ((double)e2 / poolSize) * 100, e2,
                ((double)e3 / poolSize) * 100, e3);
    }

    /**
     * Increments the level, updates enemy attributes, and plays a level up sound.
     */
    public void levelUp() {
        // Update asteroid attributes
        poolSize += 2;
        // Update level
        level++;
        // Update enemy types
        calculateEnemyDistribution(level, poolSize);
        // Play level up sound
        SoundPool.playLevelUpClip();
    }

    /**
     * Gets the number of easy enemies.
     *
     * @return The number of easy enemies.
     */
    public int getEnemyType1() {
        return enemyType1;
    }

    /**
     * Gets the number of medium enemies.
     *
     * @return The number of medium enemies.
     */
    public int getEnemyType2() {
        return enemyType2;
    }

    /**
     * Gets the number of hard enemies.
     *
     * @return The number of hard enemies.
     */
    public int getEnemyType3() {
        return enemyType3;
    }
}

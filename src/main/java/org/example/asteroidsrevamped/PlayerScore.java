package org.example.asteroidsrevamped;

/**
 * This class holds the player's score.
 *
 * @author Jose Silva
 */
public class PlayerScore implements Comparable<PlayerScore> {

    private final String playerName;
    private final int score;
    private final String level;


    /**
     * Constructs a new PlayerScore object.
     *
     * @param playerName The name of the player.
     * @param score The player's score.
     * @param level The level the score was achieved on.
     */
    public PlayerScore(String playerName, int score, String level) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the player's score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the level the score was achieved on.
     *
     * @return The level the score was achieved on.
     */
    public String getLevel() {
        return level;
    }

    /**
     * Compares this PlayerScore object to another PlayerScore object based on score.
     *
     * @param other The other PlayerScore object to compare to.
     * @return A negative integer if this score is greater than the other score,
     *         zero if the scores are equal, and a positive integer if this score
     *         is less than the other score.
     */
    @Override
    public int compareTo(PlayerScore other) {
        // Sort by score in descending order
        return Integer.compare(other.score, this.score);
    }

    /**
     * Returns a string representation of the PlayerScore object in the format:
     * playerName,score,level
     *
     * @return A string representation of the PlayerScore object.
     */
    @Override
    public String toString() {
        // Format: playerName,score,level
        return String.format("%s,%d,%s", playerName, score, level);
    }
}
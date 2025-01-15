package org.example.asteroidsrevamped;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class manages the high scores for the game.
 *
 * @author Jose Silva
 */
public class ScoreManager {
    private static final String SCORE_FILE = "highscores.txt";
    private static final int MAX_SCORES = 10;
    private List<PlayerScore> highScores;

    /**
     * Creates a ScoreManager instance and loads high scores from a file.
     */
    public ScoreManager() {
        highScores = loadScores();
    }

    /**
     * Returns a list of the current high scores.
     *
     * @return the list of PlayerScore objects
     */
    public List<PlayerScore> getHighScores() {
        return highScores;
    }

    /**
     * Adds a new player score to the high scores list.
     *
     * @param playerName the player's name
     * @param score the player's score
     * @param level the level the score was achieved on
     */
    public void addScore(String playerName, int score, String level) {
        highScores.add(new PlayerScore(playerName, score, level));
        Collections.sort(highScores); // Sort using PlayerScore's compareTo method
        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES); // Keep only top 10
        }
        saveScores();
    }

    /**
     * Loads high scores from a file.
     *
     * @return a list of PlayerScore objects loaded from the file
     */
    private List<PlayerScore> loadScores() {
        List<PlayerScore> scores = new ArrayList<>();
        File file = new File(SCORE_FILE);

        if (!file.exists()) return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length == 3) {
                    scores.add(new PlayerScore(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            parts[2]
                    ));
                }
            }
            Collections.sort(scores);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading scores: " + e.getMessage());
        }

        return scores;
    }

    /**
     * Saves the high scores to a file.
     */
    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (PlayerScore score : highScores) {
                writer.write(score.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }
}
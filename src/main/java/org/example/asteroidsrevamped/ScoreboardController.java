package org.example.asteroidsrevamped;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * This class controls the display of high scores on the Scores.fxml scene.
 */
public class ScoreboardController extends SceneController{

    @FXML
    private VBox nameBox;
    @FXML
    private VBox scoreBox;
    @FXML
    private VBox levelBox;

    /**
     * Initializes the scoreboard by populating it with high score data.
     */

    @FXML
    public void initialize() {
        // Get the children of each VBox (these will be Labels)
        List<Node> nameLabels = nameBox.getChildren();
        List<Node> scoreLabels = scoreBox.getChildren();
        List<Node> levelLabels = levelBox.getChildren();

        for (int i = 0; i < scoreManager.getHighScores().size(); i++) {
            PlayerScore score = scoreManager.getHighScores().get(i);
            Label nameLabel = (Label) nameLabels.get(i+1);
            Label scoreLabel = (Label) scoreLabels.get(i+1);
            Label levelLabel = (Label) levelLabels.get(i+1);

            nameLabel.setText(score.getPlayerName());
            scoreLabel.setText(String.valueOf(score.getScore()));
            levelLabel.setText(score.getLevel());
        }
    }
}

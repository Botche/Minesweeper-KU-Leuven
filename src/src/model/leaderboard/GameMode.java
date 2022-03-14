package model.leaderboard;

import model.Difficulty;
import utilities.constants.Common;

import java.util.ArrayList;
import java.util.List;

public class GameMode {
    private Difficulty difficulty;
    private List<PlayerScore> scores;

    public GameMode(Difficulty difficulty) {
        this.setDifficulty(difficulty);
        this.setScores(new ArrayList<>());
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    private void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<PlayerScore> getScores() {
        return scores;
    }

    private void setScores(List<PlayerScore> scores) {
        this.scores = scores;
    }

    public void addNewScore(String username, String time) {
        var playerTime = new PlayerScore(username, time);

        this.scores.add(0, playerTime);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getDifficulty());
        stringBuilder.append(System.getProperty(Common.LINE_SEPARATOR_PROPERTY));

        for (var score : this.getScores()) {
            stringBuilder.append(score.toString());
            stringBuilder.append(System.getProperty(Common.LINE_SEPARATOR_PROPERTY));
        }

        return stringBuilder.toString();
    }
}

package model.leaderboard;

import model.Difficulty;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<GameMode> scoreTables;

    public List<GameMode> getScoreTables() {
        return this.scoreTables;
    }

    public Leaderboard() {
        this.scoreTables = new ArrayList<GameMode>();
        this.scoreTables.add(new GameMode(Difficulty.EASY));
        this.scoreTables.add(new GameMode(Difficulty.MEDIUM));
        this.scoreTables.add(new GameMode(Difficulty.HARD));
    }

    public void addNewScore(Difficulty difficulty, String username, String time) {
        for (var scoreTable : this.scoreTables) {
            if (scoreTable.getDifficulty() == difficulty) {
                scoreTable.addNewScore(username, time);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (var scoreTable : this.scoreTables) {
            stringBuilder.append(scoreTable.toString());
        }

        return stringBuilder.toString();
    }
}

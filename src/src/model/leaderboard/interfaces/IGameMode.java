package model.leaderboard.interfaces;

import model.Difficulty;
import model.leaderboard.PlayerScore;

import java.util.List;

public interface IGameMode {
    Difficulty getDifficulty();
    List<PlayerScore> getScores();

    void addNewScore(String username, String time);
}

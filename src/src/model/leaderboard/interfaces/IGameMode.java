package model.leaderboard.interfaces;

import model.Difficulty;

import java.util.List;

public interface IGameMode {
    Difficulty getDifficulty();
    List<IPlayerScore> getScores();

    void addNewScore(String username, String time);
}

package model.leaderboard.interfaces;

import model.Difficulty;

import java.util.List;

public interface ILeaderboard {
    List<IGameMode> getScoreTables();
    void addNewScore(Difficulty difficulty, String username, String time);
}

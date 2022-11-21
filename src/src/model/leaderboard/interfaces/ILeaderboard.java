package model.leaderboard.interfaces;

import model.Difficulty;
import model.leaderboard.GameMode;

import java.util.List;

public interface ILeaderboard {
    List<GameMode> getScoreTables();
    void addNewScore(Difficulty difficulty, String username, String time);
}

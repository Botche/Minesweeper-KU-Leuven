package view;

import model.leaderboard.Leaderboard;
import model.leaderboard.interfaces.ILeaderboard;
import utilities.FileHelper;
import utilities.constants.Common;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Locale;

public class LeaderboardView {
    private static final String VIEW_NAME = "Leaderboard";

    public static JPanel generateView() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), VIEW_NAME, TitledBorder.CENTER, TitledBorder.TOP));

        // Read the data from the file
        var dataFromJson = (ILeaderboard) FileHelper.readFileToJson(Common.LEADERBOARD_FILE_NAME, Leaderboard.class);

        var scoreTables = dataFromJson.getScoreTables();
        String[][] data = new String[Common.MAXIMUM_RESULTS_PER_COLUMN][scoreTables.size()];
        String[] header = new String[scoreTables.size()];
        for (int index = 0; index < scoreTables.size(); index++) {
            var scoreTable = scoreTables.get(index);
            header[index] = scoreTable.getDifficulty().name().toLowerCase(Locale.ROOT);

            var scores = scoreTable.getScores();
            for (int scoresIndex = 0; scoresIndex < scores.size(); scoresIndex++) {
                var score = scores.get(scoresIndex);

                data[scoresIndex][index] = score.toString();
            }
        }

        JTable table = new JTable(data, header);
        panel.add(new JScrollPane(table));

        return panel;
    }
}

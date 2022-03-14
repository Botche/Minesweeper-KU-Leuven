package model.leaderboard;

public class PlayerScore {
    private String username;
    private String gameTime;

    public PlayerScore(String username, String gameTime) {
        this.setUsername(username);
        this.setGameTime(gameTime);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getGameTime() {
        return gameTime;
    }

    private void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getUsername());
        stringBuilder.append(" - ");
        stringBuilder.append(this.getGameTime());

        return stringBuilder.toString();
    }
}

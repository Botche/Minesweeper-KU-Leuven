package model;

import model.leaderboard.Leaderboard;
import model.leaderboard.interfaces.ILeaderboard;
import model.tiles.AbstractTile;
import model.tiles.Empty;
import model.tiles.Explosive;
import notifier.ITileStateNotifier;
import org.jetbrains.annotations.NotNull;
import utilities.FileHelper;
import utilities.Timer;
import utilities.Validator;
import utilities.constants.Common;
import utilities.constants.ErrorMessages;
import utilities.interfaces.ITimer;
import view.TileView;

import java.time.Duration;
import java.util.Random;
import java.util.TimerTask;

public class Minesweeper extends AbstractMineSweeper {
    private int countOfMines;
    private int height;
    private int width;
    private AbstractTile[][] gameBoard;
    private boolean isFirstTimeRuleEnabled = true;
    private int flagsCount;
    private ITimer timer = new Timer();
    private Difficulty difficulty;

    @Override
    public int getWidth() {
        return this.width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    private void setCountOfMines(int countOfMines) {
        this.countOfMines = countOfMines;
    }

    private void setFlagsCount(int flagsCount) {
        this.flagsCount = flagsCount;
    }

    public boolean getIsFirstTimeRuleEnabled() {
        return isFirstTimeRuleEnabled;
    }

    private void setFirstTimeRuleEnabled(boolean firstTimeRuleEnabled) {
        this.isFirstTimeRuleEnabled = firstTimeRuleEnabled;
    }

    private void setGameBoard(int row, int col) {
        boolean isBordDimensionsAreNegative = Validator.isPositive(row) == false && Validator.isPositive(col) == false;

        if (isBordDimensionsAreNegative) {
            throw new NegativeArraySizeException(ErrorMessages.NEGATIVE_NUMBER);
        }

        this.gameBoard = new AbstractTile[row][col];
    }

    private void setGameBoard(AbstractTile[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void startNewGame(Difficulty level) {
        this.initializeGameBoard(level);
        this.setFirstTimeRuleEnabled(true);
        this.fillGameBoard();

        this.viewNotifier.notifyNewGame(this.getHeight(), this.getWidth());
        this.viewNotifier.notifyFlagCountChanged(this.flagsCount);
        this.startTimer();
    }

    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        this.initializeGameBoard(row, col, explosionCount);
        this.setFirstTimeRuleEnabled(true);
        this.fillGameBoard();

        this.viewNotifier.notifyNewGame(this.getHeight(), this.getWidth());
        this.viewNotifier.notifyFlagCountChanged(this.flagsCount);
        this.startTimer();
    }

    @Override
    public void clearGame() {
        this.timer.cancel();
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        int rowBoardDimension = world.length;
        int colBoardDimension = world[0].length;

        this.setHeight(rowBoardDimension);
        this.setWidth(colBoardDimension);
        this.setFirstTimeRuleEnabled(true);

        this.setGameBoard(world);
        
        this.viewNotifier.notifyNewGame(this.getHeight(), this.getWidth());
        this.startTimer();
    }

    @Override
    public AbstractTile getTile(int x, int y) {
        if (this.areCoordinatesInvalid(x, y)) {
            return null;
        }

        AbstractTile tile = this.gameBoard[y][x];

        return tile;
    }

    @Override
    public void open(int x, int y) {
        if (this.areCoordinatesInvalid(x, y)) {
            return;
        }

        AbstractTile tile = this.gameBoard[y][x];

        if (tile.isOpened() == false && tile.isFlagged() == false) {

            if (this.getIsFirstTimeRuleEnabled()) {
                if (tile.isExplosive()) {
                    tile = this.generateEmptyTile();

                    ITileStateNotifier notifier = new TileView(x, y);
                    tile.setTileNotifier(notifier);

                    this.gameBoard[y][x] = tile;

                    this.generateMines(1, y, x);
                }

                this.deactivateFirstTileRule();
            }

            tile.open();

            if (tile.isExplosive()) {
                this.viewNotifier.notifyExploded(x, y);
                this.timer.cancel();
                this.showAllMines();
                this.viewNotifier.notifyGameLost();

                return;
            }

            int explosiveNeighboursCount = this.getExplosiveNeighboursCount(y, x);
            this.viewNotifier.notifyOpened(x, y, explosiveNeighboursCount);
            if (explosiveNeighboursCount == 0) {
                this.openAllTilesWithZeroExplosiveNeighbours(y, x);
            }

            if (this.isGameWon()) {
                this.timer.cancel();
                this.saveGameTime();
                this.viewNotifier.notifyGameWon();
            }
        }
    }

    @Override
    public void flag(int x, int y) {
        AbstractTile tile = this.gameBoard[y][x];

        if (tile.isOpened() == false) {
            tile.flag();
            --this.flagsCount;

            this.viewNotifier.notifyFlagged(x, y);
            this.viewNotifier.notifyFlagCountChanged(this.flagsCount);
        }
    }

    @Override
    public void unflag(int x, int y) {
        AbstractTile tile = this.gameBoard[y][x];

        if (tile.isOpened() == false) {
            tile.unflag();
            ++this.flagsCount;

            this.viewNotifier.notifyUnflagged(x, y);
            this.viewNotifier.notifyFlagCountChanged(this.flagsCount);
        }
    }

    @Override
    public void toggleFlag(int x, int y) {
        AbstractTile tile = this.gameBoard[y][x];

        if (tile.isOpened() == false) {
            if (tile.isFlagged()) {
                this.unflag(x, y);

                return;
            }

            this.flag(x, y);
        }
    }

    @Override
    public AbstractTile generateEmptyTile() {
        Empty emptyTile = new Empty();

        return emptyTile;
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        Explosive explosiveTile = new Explosive();

        return explosiveTile;
    }

    @Override
    public void deactivateFirstTileRule() {
        this.setFirstTimeRuleEnabled(false);
    }

    private void initializeGameBoard(@NotNull Difficulty difficulty) {
        int explosionCount = 0;

        switch(difficulty) {
            case EASY:
                this.setHeight(Common.EASY_BORD_DIMENSIONS[0]);
                this.setWidth(Common.EASY_BORD_DIMENSIONS[1]);
                explosionCount = Common.EASY_BORD_COUNT_OF_MINES;
                break;
            case MEDIUM:
                this.setHeight(Common.MEDIUM_BORD_DIMENSIONS[0]);
                this.setWidth(Common.MEDIUM_BORD_DIMENSIONS[1]);
                explosionCount = Common.MEDIUM_BORD_COUNT_OF_MINES;
                break;
            case HARD:
                this.setHeight(Common.HARD_BORD_DIMENSIONS[0]);
                this.setWidth(Common.HARD_BORD_DIMENSIONS[1]);
                explosionCount = Common.HARD_BORD_COUNT_OF_MINES;
                break;
        }

        this.difficulty = difficulty;
        this.setGameBoard(height, width);
        this.setCountOfMines(explosionCount);
        this.setFlagsCount(explosionCount);
        this.fillGameBoard();
    }

    private void initializeGameBoard(int row, int col, int explosionCount) {
        this.setHeight(row);
        this.setWidth(col);

        this.setGameBoard(row, col);
        this.setCountOfMines(explosionCount);
        this.setFlagsCount(explosionCount);
        this.fillGameBoard();
    }

    private void fillGameBoard() {
        int numberOfRows = this.getHeight();
        int numberOfColumns = this.getWidth();

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
                AbstractTile tile = this.generateEmptyTile();

                ITileStateNotifier notifier = new TileView(rowIndex, colIndex);
                tile.setTileNotifier(notifier);

                this.gameBoard[rowIndex][colIndex] = tile;
            }
        }

        this.generateMines(this.countOfMines, -1, -1);
    }

    private void generateMines(int countOfMines, int exclusiveRowIndex, int exclusiveColIndex) {
        Random randomGenerator = new Random();
        int counter = 0;

        while(counter < countOfMines)
        {
            int rowIndex = randomGenerator.nextInt(this.getHeight());
            int colIndex = randomGenerator.nextInt(this.getWidth());

            AbstractTile tile = this.gameBoard[rowIndex][colIndex];

            boolean isPositionOnExclusiveCoordinates = rowIndex == exclusiveRowIndex && colIndex == exclusiveColIndex;
            if (tile.isExplosive() || isPositionOnExclusiveCoordinates) {
                continue;
            }

            AbstractTile explosiveTile = this.generateExplosiveTile();

            ITileStateNotifier notifier = new TileView(rowIndex, colIndex);
            explosiveTile.setTileNotifier(notifier);

            this.gameBoard[rowIndex][colIndex] = explosiveTile;

            counter++;
        }
    }

    private int getExplosiveNeighboursCount(int tileRow, int tileCol) {
        int explosiveNeighboursCount = 0;

        int numberOfRows = this.getHeight();
        int numberOfColumns = this.getWidth();

        int previousRow = Math.max(tileRow - 1, 0);
        int nextRow = Math.min(tileRow + 1, numberOfRows - 1);

        int previousCol = Math.max(tileCol - 1, 0);
        int nextCol = Math.min(tileCol + 1, numberOfColumns - 1);

        for (int row = previousRow; row <= nextRow; row++) {
            for (int col = previousCol; col <= nextCol; col++) {
                AbstractTile neighbourTile = this.gameBoard[row][col];

                if (neighbourTile.isExplosive()) {
                    explosiveNeighboursCount++;
                }
            }
        }

        return explosiveNeighboursCount;
    }

    private void openAllTilesWithZeroExplosiveNeighbours(int tileRow, int tileCol) {
        int numberOfRows = this.getHeight();
        int numberOfColumns = this.getWidth();

        int previousRow = Math.max(tileRow - 1, 0);
        int nextRow = Math.min(tileRow + 1, numberOfRows - 1);

        int previousCol = Math.max(tileCol - 1, 0);
        int nextCol = Math.min(tileCol + 1, numberOfColumns - 1);

        for (int row = previousRow; row <= nextRow; row++) {
            for (int col = previousCol; col <= nextCol; col++) {
                AbstractTile neighbourTile = this.gameBoard[row][col];

                if (neighbourTile.isExplosive()) {
                    continue;
                }

                int explosiveNeighboursCount = this.getExplosiveNeighboursCount(row, col);
                if (neighbourTile.isOpened() == false) {
                    if (neighbourTile.isFlagged()) {
                        this.setFlagsCount(++this.flagsCount);
                    }

                    neighbourTile.open();
                    this.viewNotifier.notifyOpened(col, row, explosiveNeighboursCount);

                    if (explosiveNeighboursCount == 0) {
                        openAllTilesWithZeroExplosiveNeighbours(row, col);
                    }
                }
            }
        }

        this.viewNotifier.notifyFlagCountChanged(this.flagsCount);
    }

    private void showAllMines() {
        int numberOfRows = this.getHeight();
        int numberOfColumns = this.getWidth();

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
                AbstractTile tile = this.gameBoard[rowIndex][colIndex];

                if (tile.isExplosive() == false) {
                    continue;
                }

                this.viewNotifier.notifyExploded(colIndex, rowIndex);
            }
        }
    }

    private void startTimer() {
        long startingTime = System.currentTimeMillis();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                Duration duration = Duration.ofMillis(currentTime - startingTime);
                viewNotifier.notifyTimeElapsedChanged(duration);
                timer.setTimeAsString(duration);
            }
        };

        long delay = 0;
        long duration = 1000;
        this.timer.startTimer(delay, duration, task);
    }

    private void saveGameTime() {
        if (this.difficulty == null) {
            return;
        }

        // Create the file if not exist
        FileHelper.createFile(Common.LEADERBOARD_FILE_NAME);

        // Read the data from the file
        var data = (ILeaderboard)FileHelper.readDataFromFile(Common.LEADERBOARD_FILE_NAME, Leaderboard.class);

        // Update the data
        String username = System.getProperty(Common.USERNAME_PROPERTY);
        data.addNewScore(this.difficulty, username, this.timer.getTimeAsString());

        // Write the data to the file
        FileHelper.writeDataToFile(Common.LEADERBOARD_FILE_NAME, data);
    }

    private boolean isGameWon() {
        int numberOfRows = this.getHeight();
        int numberOfColumns = this.getWidth();

        boolean isAllEmptyTilesAreOpen = true;

        rowloop:
        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
                var tile = this.gameBoard[rowIndex][colIndex];

                if (tile.isOpened() == false && tile.isExplosive() == false) {
                    isAllEmptyTilesAreOpen = false;
                    break rowloop;
                }
            }
        }

        return isAllEmptyTilesAreOpen;
    }

    private boolean areCoordinatesInvalid(int x, int y) {
        boolean isXInvalid = Validator.isPositive(x) == false
                || Validator.isGreaterThan(x, this.getWidth() - 1);

        boolean isYInvalid = Validator.isPositive(y) == false
                || Validator.isGreaterThan(y, this.getHeight() - 1);

        return isXInvalid || isYInvalid;
    }
}

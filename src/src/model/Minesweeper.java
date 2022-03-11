package model;

import model.tiles.AbstractTile;
import model.tiles.Empty;
import model.tiles.Explosive;
import model.tiles.Tile;
import notifier.ITileStateNotifier;
import org.jetbrains.annotations.NotNull;
import utilities.constants.Common;
import utilities.Validator;
import utilities.constants.ErrorMessages;
import view.TileView;

import java.util.Random;

public class Minesweeper extends AbstractMineSweeper {
    private int width;
    private int height;
    private int countOfMines;
    private int rowBoardDimension = 0;
    private int colBoardDimension = 0;
    private AbstractTile[][] gameBoard;
    private boolean isFirstTimeRuleEnabled;

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

    private void setFirstTimeRuleEnabled(boolean firstTimeRuleEnabled) {
        this.isFirstTimeRuleEnabled = firstTimeRuleEnabled;
    }

    private void setGameBoard(int row, int col) {
        boolean isBordDimensionsAreNegative = Validator.IsPositive(row) == false && Validator.IsPositive(col) == false;

        if (isBordDimensionsAreNegative) {
            throw new NegativeArraySizeException(ErrorMessages.NEGATIVE_NUMBER);
        }

        this.gameBoard = new AbstractTile[row][col];
    }

    private void setGameBoard(AbstractTile[][] gameBoard) {
        // TODO: Check if gameBoard is null

        this.gameBoard = gameBoard;
    }

    @Override
    public void startNewGame(Difficulty level) {
        this.initializeGameBoard(level);
        this.fillGameBoard();
    }

    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        this.initializeGameBoard(row, col, explosionCount);
        this.fillGameBoard();
    }

    @Override
    public void toggleFlag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            if (tile.isFlagged()) {
                tile.unflag();
                this.viewNotifier.notifyUnflagged(x, y);

                return;
            }

            tile.flag();
            this.viewNotifier.notifyFlagged(x, y);
        }
    }

    @Override
    public AbstractTile getTile(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        return tile;
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        this.setGameBoard(world);
    }

    @Override
    public void open(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false && tile.isFlagged() == false) {
            tile.open();

            if (tile.isExplosive()) {
                this.viewNotifier.notifyExploded(x, y);
                this.viewNotifier.notifyGameLost();
            } else {
                this.viewNotifier.notifyOpened(x, y, 0);
            }
        }
    }

    @Override
    public void flag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.flag();

            this.viewNotifier.notifyFlagged(x, y);
        }
    }

    @Override
    public void unflag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.unflag();

            this.viewNotifier.notifyUnflagged(x, y);
        }
    }

    @Override
    public void deactivateFirstTileRule() {
        this.setFirstTimeRuleEnabled(false);
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

    private void initializeGameBoard(@NotNull Difficulty difficulty) {
        int explosionCount = 0;

        switch(difficulty) {
            case EASY:
                this.rowBoardDimension = Common.EASY_BORD_DIMENSIONS[0];
                this.colBoardDimension = Common.EASY_BORD_DIMENSIONS[1];
                explosionCount = Common.EASY_BORD_COUNT_OF_MINES;
                break;
            case MEDIUM:
                this.rowBoardDimension = Common.MEDIUM_BORD_DIMENSIONS[0];
                this.colBoardDimension = Common.MEDIUM_BORD_DIMENSIONS[1];
                explosionCount = Common.MEDIUM_BORD_COUNT_OF_MINES;
                break;
            case HARD:
                this.rowBoardDimension = Common.HARD_BORD_DIMENSIONS[0];
                this.colBoardDimension = Common.HARD_BORD_DIMENSIONS[1];
                explosionCount = Common.HARD_BORD_COUNT_OF_MINES;
                break;
        }

        this.setGameBoard(rowBoardDimension, colBoardDimension);
        this.setCountOfMines(explosionCount);
        this.fillGameBoard();
    }

    private void initializeGameBoard(int row, int col, int explosionCount) {
        this.setGameBoard(row, col);
        this.setCountOfMines(explosionCount);
        this.fillGameBoard();
    }

    private void fillGameBoard() {
        int numberOfRows = this.gameBoard.length;
        int numberOfColumns = this.gameBoard[0].length;

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
                AbstractTile tile = this.generateEmptyTile();

                ITileStateNotifier notifier = new TileView(rowIndex, colIndex);
                tile.setTileNotifier(notifier);

                this.gameBoard[rowIndex][colIndex] = tile;
            }
        }

        this.generateBoardMines();
    }

    private void generateBoardMines() {
        Random randomGenerator = new Random();
        int counter = 0;

        while(counter <= this.countOfMines)
        {
            int rowIndex = randomGenerator.nextInt(this.rowBoardDimension);
            int colIndex = randomGenerator.nextInt(this.colBoardDimension);

            AbstractTile tile = this.gameBoard[rowIndex][colIndex];
            String tileName = tile.getClass().getSimpleName();

            if (tileName.equals("Explosive")) {
                continue;
            }

            AbstractTile explosiveTile = this.generateExplosiveTile();

            ITileStateNotifier notifier = new TileView(rowIndex, colIndex);
            explosiveTile.setTileNotifier(notifier);

            this.gameBoard[rowIndex][colIndex] = explosiveTile;

            counter++;
        }
    }
}

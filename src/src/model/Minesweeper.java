package model;

import model.tiles.AbstractTile;
import model.tiles.Empty;
import model.tiles.Explosive;
import org.jetbrains.annotations.NotNull;
import utilities.constants.Common;
import utilities.Validator;
import utilities.constants.ErrorMessages;

public class Minesweeper extends AbstractMineSweeper {
    private int width;
    private int height;
    private int countOfMines;
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

        if (tile.isFlagged()) {
            tile.unflag();

            return;
        }

        tile.flag();
    }

    @Override
    public AbstractTile getTile(int x, int y) {
        return null;
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        this.setGameBoard(world);
    }

    @Override
    public void open(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.open();
        }
    }

    @Override
    public void flag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.flag();
        }
    }

    @Override
    public void unflag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.unflag();
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
        int rowBoardDimension = 0;
        int colBoardDimension = 0;

        switch(difficulty) {
            case EASY:
                rowBoardDimension = Common.EASY_BORD_DIMENSIONS[0];
                colBoardDimension = Common.EASY_BORD_DIMENSIONS[1];
                explosionCount = Common.EASY_BORD_COUNT_OF_MINES;
                break;
            case MEDIUM:
                rowBoardDimension = Common.MEDIUM_BORD_DIMENSIONS[0];
                colBoardDimension = Common.MEDIUM_BORD_DIMENSIONS[1];
                explosionCount = Common.MEDIUM_BORD_COUNT_OF_MINES;
                break;
            case HARD:
                rowBoardDimension = Common.HARD_BORD_DIMENSIONS[0];
                colBoardDimension = Common.HARD_BORD_DIMENSIONS[1];
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
                this.gameBoard[rowIndex][colIndex] = this.generateEmptyTile();
            }
        }
    }
}

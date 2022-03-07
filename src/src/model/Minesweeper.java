package model;

import org.jetbrains.annotations.NotNull;
import utilities.constants.Common;
import utilities.Validator;
import utilities.constants.ErrorMessages;

public class Minesweeper extends AbstractMineSweeper {
    private int width;
    private int height;
    private int explosionCount;
    private int[][] gameBoard;

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

    private void setExplosionCount(int explosionCount) {
        this.explosionCount = explosionCount;
    }

    private void setGameBoard(int row, int col) {
        boolean isBordDimensionsAreNegative = Validator.IsPositive(row) && Validator.IsPositive(col);

        if (isBordDimensionsAreNegative) {
            throw new NegativeArraySizeException(ErrorMessages.NEGATIVE_NUMBER);
        }

        this.gameBoard = new int[row][col];
    }

    @Override
    public void startNewGame(Difficulty level) {
        initializeGameBoard(level);
    }

    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        initializeGameBoard(row, col);
    }

    @Override
    public void toggleFlag(int x, int y) {

    }

    @Override
    public AbstractTile getTile(int x, int y) {
        return null;
    }

    @Override
    public void setWorld(AbstractTile[][] world) {

    }

    @Override
    public void open(int x, int y) {

    }

    @Override
    public void flag(int x, int y) {

    }

    @Override
    public void unflag(int x, int y) {

    }

    @Override
    public void deactivateFirstTileRule() {

    }

    @Override
    public AbstractTile generateEmptyTile() {
        return null;
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        return null;
    }

    private void initializeGameBoard(@NotNull Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> this.setGameBoard(Common.EASY_BORD_DIMENSIONS[0], Common.EASY_BORD_DIMENSIONS[1]);
            case MEDIUM -> this.setGameBoard(Common.MEDIUM_BORD_DIMENSIONS[0], Common.MEDIUM_BORD_DIMENSIONS[1]);
            case HARD -> this.setGameBoard(Common.HARD_BORD_DIMENSIONS[0], Common.HARD_BORD_DIMENSIONS[1]);
        }
    }

    private void initializeGameBoard(int row, int col) {
        this.setGameBoard(row, col);
    }
}

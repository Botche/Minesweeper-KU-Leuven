package model;

import model.tiles.AbstractTile;
import model.tiles.Empty;
import model.tiles.Explosive;
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
    private int flagCount;

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
        boolean isBordDimensionsAreNegative = Validator.isPositive(row) == false && Validator.isPositive(col) == false;

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
    public AbstractTile getTile(int x, int y) {
        if (this.areCoordinatesInvalid(x, y)) {
            return null;
        }

        AbstractTile tile = this.gameBoard[x][y];

        return tile;
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        this.setGameBoard(world);
    }

    @Override
    public void open(int x, int y) {
        if (this.areCoordinatesInvalid(x, y)) {
            return;
        }

        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false && tile.isFlagged() == false) {
            tile.open();

            if (tile.isExplosive()) {
                this.viewNotifier.notifyExploded(x, y);
                this.viewNotifier.notifyGameLost();
            } else {
                int explosiveNeighboursCount = this.getExplosiveNeighboursCount(x, y);
                this.viewNotifier.notifyOpened(x, y, explosiveNeighboursCount);
            }
        }
    }

    @Override
    public void flag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.flag();
            ++this.flagCount;

            this.viewNotifier.notifyFlagged(x, y);
            this.viewNotifier.notifyFlagCountChanged(this.flagCount);
        }
    }

    @Override
    public void unflag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            tile.unflag();
            --this.flagCount;

            this.viewNotifier.notifyUnflagged(x, y);
            this.viewNotifier.notifyFlagCountChanged(this.flagCount);
        }
    }

    @Override
    public void toggleFlag(int x, int y) {
        AbstractTile tile = this.gameBoard[x][y];

        if (tile.isOpened() == false) {
            if (tile.isFlagged()) {
                this.unflag(x, y);

                return;
            }

            this.flag(x, y);
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
        this.rowBoardDimension = row;
        this.colBoardDimension = col;

        this.setGameBoard(row, col);
        this.setCountOfMines(explosionCount);
        this.fillGameBoard();
    }

    private void fillGameBoard() {
        int numberOfRows = this.rowBoardDimension;
        int numberOfColumns = this.colBoardDimension;

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

    private int getExplosiveNeighboursCount(int x, int y) {
        int explosiveNeighboursCount = 0;

        int numberOfRows = this.rowBoardDimension;
        int numberOfColumns = this.colBoardDimension;

        int previousRow = Math.max(x - 1, 0);
        int nextRow = Math.min(x + 1, numberOfRows - 1);

        int previousCol = Math.max(y - 1, 0);
        int nextCol = Math.min(y + 1, numberOfColumns - 1);

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

    private boolean areCoordinatesInvalid(int x, int y) {
        boolean isXInvalid = Validator.isPositive(x) == false
                || Validator.isGreaterThan(x, this.rowBoardDimension - 1);

        boolean isYInvalid = Validator.isPositive(y) == false
                || Validator.isGreaterThan(y, this.colBoardDimension - 1);

        return isXInvalid || isYInvalid;
    }
}

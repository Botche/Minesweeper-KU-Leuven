package test;

import model.PlayableMinesweeper;
import model.tiles.AbstractTile;

public interface TestableMinesweeper extends PlayableMinesweeper {
    AbstractTile getTile(int x, int y);
    void setWorld(AbstractTile[][] world);
    void open(int x, int y);
    void flag(int x, int y);
    void unflag(int x, int y);
    void deactivateFirstTileRule();
    AbstractTile generateEmptyTile();
    AbstractTile generateExplosiveTile();
}

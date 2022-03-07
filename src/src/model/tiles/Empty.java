package model.tiles;

public class Empty extends AbstractTile {
    private boolean isFlagged;
    private boolean isExplosive;
    private boolean isOpened;

    public Empty() {
        this.isFlagged = false;
        this.isExplosive = false;
        this.isOpened = false;
    }

    @Override
    public boolean isFlagged() {
        return this.isFlagged;
    }

    @Override
    public boolean isExplosive() {
        return this.isExplosive;
    }

    @Override
    public boolean isOpened() {
        return this.isOpened;
    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public void flag() {

    }

    @Override
    public void unflag() {

    }
}

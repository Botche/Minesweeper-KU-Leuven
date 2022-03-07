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

    private void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    @Override
    public boolean isExplosive() {
        return this.isExplosive;
    }

    private void setIsExplosive(boolean isExplosive) {
        this.isExplosive = isExplosive;
    }

    @Override
    public boolean isOpened() {
        return this.isOpened;
    }

    private void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public boolean open() {
        this.setIsOpened(true);

        return this.isOpened();
    }

    @Override
    public void flag() {
        this.setIsFlagged(true);
    }

    @Override
    public void unflag() {
        this.setIsFlagged(false);
    }
}

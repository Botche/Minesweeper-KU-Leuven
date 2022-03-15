package model.tiles;

public abstract class Tile extends AbstractTile {
    private boolean isFlagged;
    private boolean isExplosive;
    private boolean isOpened;

    protected Tile(boolean isFlagged, boolean isExplosive, boolean isOpened) {
        this.setIsFlagged(isFlagged);
        this.setIsExplosive(isExplosive);
        this.setIsOpened(isOpened);
    }

    @Override
    public boolean isFlagged() {
        return this.isFlagged;
    }

    protected void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    @Override
    public boolean isExplosive() {
        return this.isExplosive;
    }

    protected void setIsExplosive(boolean isExplosive) {
        this.isExplosive = isExplosive;
    }

    @Override
    public boolean isOpened() {
        return this.isOpened;
    }

    protected void setIsOpened(boolean isOpened) {
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

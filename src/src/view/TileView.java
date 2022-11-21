package view;

import notifier.ITileStateNotifier;
import utilities.constants.Common;
import view.MinesweeperView.AssetPath;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.io.IOException;

public class TileView extends JButton implements ITileStateNotifier {
    private static ImageIcon flagIcon;
    private static ImageIcon bombIcon;

    static {
        try {
            flagIcon = new ImageIcon(ImageIO.read(TileView.class.getResource(AssetPath.FLAG_ICON)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            bombIcon = new ImageIcon(ImageIO.read(TileView.class.getResource(AssetPath.BOMB_ICON)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int x, y;
    public TileView(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getPositionX() {return x;}
    public int getPositionY() {return y;}

    @Override
    public void notifyOpened(int explosiveNeighbourCount) {
        super.setIcon(null);
        super.setText((explosiveNeighbourCount> 0)? Integer.toString(explosiveNeighbourCount) : "");
        super.setEnabled(false);
    }

    @Override
    public void notifyFlagged() {
        super.setIcon(flagIcon);
    }

    @Override
    public void notifyUnflagged() {
        super.setIcon(null);
    }

    public void removalAllMouseListeners() {
        for (MouseListener listener : super.getMouseListeners()) 
            this.removeMouseListener(listener);    
    }

    @Override
    public void notifyExplode() {
        super.setText("");
        super.setIcon(bombIcon);
        super.setEnabled(false);
    }

    @Override
    public String toString(){
        return "["+Integer.toString(x)+","+Integer.toString(y)+"]";
    }
    
}

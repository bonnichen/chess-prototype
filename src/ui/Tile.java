package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.Board.iPair;

public class Tile {
    private final iPair location;
    private boolean isHighlighted;
    private Color highlightColor = null;
    public Tile(iPair location) {
        this.location = location;
        isHighlighted = false;
    }

    public void paint(Graphics2D g2) {
        Color baseColor;
        if ((location.i() + location.j()) %2 == 0) {
            baseColor = Color.lightGray;
        }else {
            baseColor = Color.darkGray;
        }
        g2.setColor(baseColor);
        if (isHighlighted){
            g2.setColor(highlightColor);
        }
        g2.fill(new Rectangle(location.i()*75, location.j()*75, 75, 75));
    }

    public void setCheckHighlight(){
        isHighlighted = true;
        highlightColor = Color.red;
    }

    public void setValidMoveHighlight(){
        isHighlighted = true;
        highlightColor = Color.yellow;
    }

    public void setIsSelected(){
        isHighlighted = true;
        highlightColor = Color.yellow;
    }

    public void removeHighlight(){
        isHighlighted = false;
        highlightColor = null;
    }
}

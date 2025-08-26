package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import model.Board.iPair;
import model.Player;

public class PromotionScreen extends JPanel {
    private final Player player;
    public PromotionScreen (Player player){
        setOpaque(false);
        setBounds(0, 0, 600, 600);
        this.player = player;
    }
    public void paintPromotionBoard(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setColor(new Color(100,100,100));
        g2.fillRect(0,0, 600,600);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setColor(new Color(200,200,200));
        g2.fillRect(220, 220, 160, 160);
//        g2.setColor(new Color(103,127,163));
//        g2.fillRect(225, 225, 75, 75);
//        g2.fillRect(300, 225, 75, 75);
//        g2.fillRect(225, 300, 75, 75);
//        g2.fillRect(300, 300, 75, 75);
        paintIcons(g2);
    }

    public void paintIcons (Graphics2D g2){
        GameBoard.paintQueen(g2, new iPair(3,3), player);
        GameBoard.paintRook(g2, new iPair(3,4), player);
        GameBoard.paintBishop(g2, new iPair(4,3), player);
        GameBoard.paintKnight(g2, new iPair(4,4), player);
    }

    public static void removePromo(){

    }

    @Override
    public void paintComponent(Graphics g) {
    paintPromotionBoard(g);
    }

}

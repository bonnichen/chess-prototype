package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.JPanel;
import model.Bishop;
import model.Board.GraphCoord;
import model.Board.iPair;
import model.GameModel;
import model.King;
import model.Knight;
import model.Pawn;
import model.Pieces;
import model.Player;
import model.Queen;
import model.Rook;

public class GameBoard extends JPanel {

    private final GameController controller;

    private static Tile[][] tileGrid;
    private final GameModel model;
    private PromotionScreen promo;
    private final PropertyChangeListener boardListener;


    public GameBoard(GameModel model) {
        promo = null;
        tileGrid = new Tile[8][8];
        controller = new GameController(model, this);
        this.model = model;
        setModel();
        addMouseListener(controller);
        boardListener = e -> {
            repaint();
        };
        model.addPropertyChangeListener("boardState", boardListener);
        controller.addPropertyChangeListener("boardState", boardListener);
    }

    public void setModel() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tileGrid[i][j] = new Tile(new iPair(i,j));
                }
            }
            setPreferredSize(new Dimension(600, 600));
    }

    public void paintPawn(Graphics2D g2, iPair pair, Player player){
        int y = pair.j() * 75;
        int x = (600 * (pair.i()+1) / 8 - 75);
        g2.setColor(setUpColor(player));
        //head
        g2.fill(new Arc2D.Double(x + 26, y + 13, 23, 23, 0, 180, Arc2D.PIE));
        //body
        g2.fill(new Rectangle(x + 26, y + 26, 23, 5));
        int[] xPoints = {x + 29, x + 46, x + 51, x+24};
        int[] yPoints = {y + 28, y + 28, y + 53, y + 53};
        g2.fillPolygon(xPoints, yPoints, 4);
        g2.fill(new Rectangle(x + 21, y + 53, 33, 7));
    }

    public static void paintRook(Graphics2D g2, iPair pair, Player player){
        int y = pair.j() * 75;
        int x = (600 * (pair.i()+1) / 8 - 75);
        g2.setColor(setUpColor(player));
        //head
        g2.fill(new Rectangle(x + 26, y + 13, 3, 5));
        g2.fill(new Rectangle(x + 36, y + 13, 3, 5));
        g2.fill(new Rectangle(x + 46, y + 13, 3, 5));
        g2.fill(new Rectangle(x + 26, y + 18, 23, 4));
        //body
        g2.fill(new Rectangle(x+27, y + 24, 21, 4));
        int[] xPoints = {x + 29, x + 46, x + 51, x+24};
        int[] yPoints = {y + 28, y + 28, y + 53, y + 53};
        g2.fillPolygon(xPoints, yPoints, 4);
        g2.fill(new Rectangle(x + 21, y + 53, 33, 7));
    }

    public static void paintKnight(Graphics2D g2, iPair pair, Player player){
        int y = pair.j() * 75;
        int x = (600 * (pair.i()+1) / 8 - 75);
        g2.setColor(setUpColor(player));
        //head

        int[] xPoints = {x + 19, x + 19, x + 33, x + 33, x + 23, x + 21};
        int[] yPoints = {y + 33, y + 29, y + 15, y + 25, y + 35, y + 35};
        g2.fillPolygon(xPoints, yPoints, 6);
        xPoints = new int[]{x + 20, x + 38, x + 36, x + 32};
        yPoints = new int[]{y + 31, y + 15, y + 28, y + 31};
        g2.fillPolygon(xPoints, yPoints, 4);
        xPoints = new int[]{x + 33, x + 41, x + 41, x + 33};
        yPoints = new int[]{y + 15, y + 15, y + 25, y + 25};
        g2.fillPolygon(xPoints, yPoints, 4);
        xPoints = new int[]{x + 28, x + 32, x + 29};
        yPoints = new int[]{y + 13, y + 18, y + 19};
        g2.fillPolygon(xPoints, yPoints, 3);
        xPoints = new int[]{x + 35, x + 41, x + 53, x + 53, x + 39};
        yPoints = new int[]{y + 15, y + 15, y + 27, y + 30, y + 30};
        g2.fillPolygon(xPoints, yPoints, 5);
        //body
        xPoints = new int[]{x + 39, x + 53, x + 53, x + 42};
        yPoints = new int[]{y + 29, y + 29, y + 32, y + 32};
        g2.fillPolygon(xPoints, yPoints, 4);

        xPoints = new int[]{x + 42, x + 53, x + 53, x + 20};
        yPoints = new int[]{y + 27, y + 27, y + 49, y + 49};
        g2.fillPolygon(xPoints, yPoints, 4);
        xPoints = new int[]{x + 20, x + 53, x + 53, x + 28};
        yPoints = new int[]{y + 49, y + 49, y + 57, y + 57};
        g2.fillPolygon(xPoints, yPoints, 4);
        xPoints = new int[]{x + 25, x + 55, x + 55, x + 25};
        yPoints = new int[]{y + 57, y + 57, y + 61, y + 61};
        g2.fillPolygon(xPoints, yPoints, 4);
        //mane
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x + 37, y + 13, x + 41, y + 13);
        g2.drawLine(x + 41, y + 13, x + 55, y + 26);
        g2.drawLine(x + 55, y + 26, x + 55, y + 44);
    }

    public static void paintBishop(Graphics2D g2, iPair pair, Player player) {
        int y = pair.j() * 75;
        int x = (600 * (pair.i() + 1) / 8 - 75);
        g2.setColor(setUpColor(player));

        //Head
        g2.fill(new Arc2D.Double(x + 32, y + 14, 11, 11, 0, 360, Arc2D.PIE));

        //Body
        Path2D head = new Path2D.Float();
        head.moveTo(x + 34, y + 21);
        head.curveTo(x + 34, y + 21, x + 17, y + 40, x + 30 , y + 53);
        head.lineTo(x + 49, y + 53);
        head.curveTo(x + 49, y + 53, x + 59, y + 40, x + 46 , y + 27);
        head.curveTo(x + 46 , y + 27, x + 42, y + 30, x + 42 , y + 40);
        head.lineTo(x + 38, y + 40);
        head.curveTo(x + 38, y + 40, x + 32, y + 40, x + 42 , y + 20);
        head.closePath();
        g2.fill(head);
        g2.fill(new Rectangle(x + 23, y + 53, 33, 7));
    }
    public static void paintQueen(Graphics2D g2, iPair pair, Player player) {
        int y = pair.j() * 75;
        int x = (600 * (pair.i() + 1) / 8 - 75);
        g2.setColor(setUpColor(player));

        //Head
        g2.fill(new Arc2D.Double(x + 34, y + 15, 7, 7, 0, 360, Arc2D.PIE));
        int[] xPoints = {x + 28, x + 38, x + 48};
        int[] yPoints = {y + 53, y + 24, y + 53};
        g2.fillPolygon(xPoints,yPoints,3);

        //Crown
        xPoints = new int[]{x + 20, x + 17, x + 40};
        yPoints = new int[]{y + 53, y + 33, y + 53};
        g2.fillPolygon(xPoints,yPoints,3);

        xPoints = new int[]{x + 56, x + 59, x + 36};
        yPoints = new int[]{y + 53, y + 33, y + 53};
        g2.fillPolygon(xPoints,yPoints,3);

        xPoints = new int[]{x + 30, x + 25, x + 25, x + 32};
        yPoints = new int[]{y + 42, y + 37, y + 28, y + 35};
        g2.fillPolygon(xPoints,yPoints,4);

        xPoints = new int[]{x + 46, x + 44, x + 51, x + 51};
        yPoints = new int[]{y + 42, y + 35, y + 28, y + 37};
        g2.fillPolygon(xPoints,yPoints,4);

        g2.fill(new Rectangle(x + 20, y + 54, 36, 6));
    }

    public void paintKing(Graphics2D g2, iPair pair, Player player) {
        int y = pair.j() * 75;
        int x = (600 * (pair.i() + 1) / 8 - 75);
        g2.setColor(setUpColor(player));

        //cross
        g2.fillRect(x + 37, y + 15, 3, 12);
        g2.fillRect(x + 35, y + 18, 7, 3);

        Path2D crownC = new Path2D.Float();
        crownC.moveTo(x + 28, y + 35);
        crownC.curveTo(x + 28, y + 35, x + 38, y + 11, x + 48 , y + 35);
        crownC.curveTo(x + 48 , y + 35, x + 39, y + 40, x + 38 , y + 53);
        crownC.curveTo(x + 38 , y + 53,  x + 37, y + 40, x + 28, y + 35);
        g2.fill(crownC);

        Path2D crownR = new Path2D.Float();
        crownR.moveTo(x + 40.5, y + 53);
        crownR.curveTo(x + 40.5, y + 50, x + 42.5, y + 41, x + 48.5, y + 37);
        crownR.curveTo(x + 48.5, y + 37, x + 53.5, y + 33, x + 58.5, y + 37);
        crownR.curveTo(x + 58.5, y + 37, x + 62.5, y + 42, x + 53.5, y + 53);
        crownR.closePath();
        g2.fill(crownR);


        Path2D crownL = new Path2D.Float();
        crownL.moveTo(x + 36, y + 53);
        crownL.curveTo(x + 36, y + 50, x + 34, y + 41, x + 28 , y + 37);
        crownL.curveTo(x + 28 , y + 37, x + 23, y + 33, x + 18 , y + 37);
        crownL.curveTo(x + 18 , y + 37, x + 14, y + 42, x + 23 , y + 53);
        crownL.closePath();
        g2.fill(crownL);


        g2.fill(new Rectangle(x + 23, y + 54, 31, 5));

    }

    public void paintPieces(Graphics2D g2){
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Pieces[] chessPieces = model.getChessPieces();
        for (Pieces piece: chessPieces){
            if (piece instanceof Pawn) {
                paintPawn(g2, piece.getCoord(), piece.getPlayer());
            } else if (piece instanceof Rook) {
                paintRook(g2, piece.getCoord(), piece.getPlayer());
            } else if (piece instanceof Knight) {
                paintKnight(g2, piece.getCoord(), piece.getPlayer());
            } else if (piece instanceof Bishop){
                paintBishop(g2, piece.getCoord(), piece.getPlayer());
            } else if (piece instanceof Queen){
                paintQueen(g2, piece.getCoord(), piece.getPlayer());
            } else if (piece instanceof King){
                paintKing(g2, piece.getCoord(), piece.getPlayer());
            }
        }

    }

    private static Color setUpColor(Player player){
        Color color;
        if (Objects.equals(player.getColor(), "BLACK")) {
            color = Color.black;
        } else {
            color = Color.white;
        }
        return color;
    }

    public void updateBoard(){
        repaint();
    }

    public void setValidMoves(GraphCoord[] moves){
        if (moves != null) {
            for (GraphCoord move : moves) {
                tileGrid[move.getCoord().i()][move.getCoord().j()].setValidMoveHighlight();
            }
        }
    }

    public void removeHighlights(){
        for (Tile[] tiles : tileGrid) {
            for(Tile tile: tiles){
                tile.removeHighlight();
            }
        }
    }

    public void setUnderCheck(GraphCoord[] pieces){
        for (GraphCoord move : pieces){
            tileGrid[move.getCoord().i()][move.getCoord().j()].setCheckHighlight();
        }
    }

    void paintUnderCheck(Pieces[] pieces, Graphics2D g2){
        for (Pieces piece: pieces) {
            iPair pos = piece.getCoord();
            tileGrid[pos.i()][pos.j()].setCheckHighlight();
        }
        paintPieces(g2);
        updateBoard();
    }

    public void showPromotionScreen(Player player){
        PromotionScreen promo = new PromotionScreen(player);
        this.promo = promo;
        add(promo);
        setLayout(null);
        repaint();
        revalidate();

    }

    public void removePromotionScreen(){
        remove(promo);
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Tile[] row : tileGrid) {
            for (Tile tile : row) {
                tile.paint(g2);
            }
        }
        paintPieces(g2);
    }
}

package model;

import java.util.Objects;
import model.Board.GraphCoord;
import model.Board.iPair;

public abstract class Pieces {

    protected final Player player;
    protected GraphCoord pos;
    protected final GraphCoord startPos;
    protected final Pieces[][] board;
    protected final GameModel model;
    protected boolean moved;
    protected int value;

    private final King king;

    protected Move lastMove;

    public Move getLastMove(){
        return lastMove;
    }

    public void setMovedFalse(){
        moved = false;
    }

    public int getValue(){
        return value;
    }

    public abstract GraphCoord[] moves();

    protected boolean checkValidMove(iPair coord, int x, int y){
        return coord.i() + x >= 0 && coord.i() + x < 8 &&
                coord.j() + y >= 0 && coord.j() + y < 8 &&
                (board[coord.i() + x][coord.j() + y]==null ||
                board[coord.i() + x][coord.j() + y].getPlayer() != player);
    }
    protected boolean kingNotInCheck(int i, int j){
        iPair coord = pos.getCoord();
        Pieces pieceMoved = movePiece(this, i, j);
        boolean output = king.underCheck();
        board[coord.i()][coord.j()] = this;
        pos.setCoord(coord);
        board[i][j] = pieceMoved;
        return !output;
    }

    protected boolean checkCapture(iPair coord, int x, int y){
        Pieces piece = board[coord.i() + x][coord.j() + y];
        return piece != null && piece.getPlayer()!= this.player;
    }

    public void setPos(int i, int j){
        boolean isFirstMove = (!moved);
        lastMove = new Move(new GraphCoord(pos.getCoord()),
                new GraphCoord(new iPair(i,j)),this, model.getLastMove());
        if (canCapture(i,j)){
            processCapture(i,j);
        }
        specialCases(i, j);
        movePiece(this, i, j);
        lastMove.setWasFirstMove(isFirstMove);
        moved = true;
    }

    public Player getPlayer(){
        return player;
    }

    private boolean canCapture(int i, int j){
        return board[i][j] != null && board[i][j].player != player;
    }

    protected Pieces movePiece(Pieces pieceMoved, int i, int j){
        iPair coord = pieceMoved.getPos().getCoord();
        board[coord.i()][coord.j()] = null;
        Pieces output = board[i][j];
        pieceMoved.getPos().setCoord(new iPair(i,j));
        board[i][j] = pieceMoved;
        return output;
    }

    protected void specialCases(int i, int j){
    }



    protected Pieces processCapture(int i, int j){
        Pieces output = board[i][j];
        model.removePiece(board[i][j]);
        board[i][j] = null;
        lastMove.setPieceCaptured(output);
        return output;
    }
    public void setValidMoves(){
        player.setValidMoves(moves());

    }
    public iPair getCoord(){
        return pos.getCoord();
    }
    public GraphCoord getPos(){
        return pos;
    }

    protected int getLastRow(){
        int row = 0;
        if (Objects.equals(player.getColor(), "WHITE")){
            row = 7;
        }
        return row;
    }
    public Pieces(GraphCoord pos, Player player, GameModel model){
        this.pos = pos;
        this.startPos = new GraphCoord(pos.getCoord());
        this.player = player;
        this.model = model;
        moved = false;
        king = model.getKing(player);
        board = model.getBoard();
        lastMove = null;


    }

}

package model;

import model.Board.GraphCoord;

public class Move {
    private final GraphCoord positionFrom;
    private final GraphCoord positionTo;
    private final Pieces pieceMoved;
    private final Move lastMove;
    private Pieces pieceCaptured;
    private boolean wasFirstMove;
    private boolean wasPromoted;
    private Pieces promotedTo;
    private int capturedIndex;
    private boolean wasEnPassant;
    private boolean wasCastling;
    private int score;

    public int capturedIndex(){
        return capturedIndex;
    }
    
    public int score(){return score;}

    public void setScore(int score) {
        this.score = score;
    }

    public Pieces pieceMoved() {
        return pieceMoved;
    }

    public GraphCoord positionFrom(){
        return positionFrom;
    }

    public GraphCoord positionTo(){
        return positionTo;
    }

    public boolean WasFirstMove(){
        return wasFirstMove;
    }
    public void setWasFirstMove(boolean moved){
        wasFirstMove = moved;
    }

    public boolean wasPromoted(){
        return wasPromoted;
    }
    public void setWasPromoted(boolean promoted){
        wasPromoted = promoted;
    }
    public Pieces promotedTo(){
        return promotedTo;
    }
    public void setPromotedTo(Pieces promoted){
        promotedTo = promoted;
    }

    public boolean wasCastling(){
        return wasCastling;
    }
    public void setWasCastling(boolean castling){
        wasCastling = castling;
    }

    public void setWasEnPassant(boolean enPassant){
        wasEnPassant = enPassant;
    }

    public Pieces pieceCaptured(){
        return pieceCaptured;
    }

    public void setPieceCaptured(Pieces captured, int index){
        pieceCaptured = captured;
        capturedIndex = index;
    
    }

    public Move lastMove(){
        return lastMove;
    }

    public Move(GraphCoord positionFrom, GraphCoord positionTo, Pieces pieceMoved, Move lastMove){
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
        this.pieceMoved = pieceMoved;
        this.lastMove = lastMove;
    }
}

package model;

import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;
import model.Board.GraphCoord;
import model.Board.iPair;

public class GameModel {

    public enum GameState {READY, PLAYING, CHECKMATE, STALEMATE, INSUFFICIENT_MATERIAL}
    public enum moveType {INVALID, VALID, PROMOTION, AIMOVE}
    private final Player player1;
    private final Player player2;
    private Pieces[] chessPieces;
    private GameState state;
    private King whiteKing;
    private King blackKing;
    private Move lastMove;
    private Player playerInPlay;
    protected SwingPropertyChangeSupport propSupport;
    private final Pieces[][] board;
    public Pieces[][] getBoard(){
        return board;
    }
    public King getKing(Player player){
        if (player == player2){
            return blackKing;
        }
        return whiteKing;
    }
    public void setLastMove(Move move){
        lastMove = move;
    }

    public Player getEnemyPlayer(){
        if (playerInPlay == player1){
            return player2;
        }
        return player1;
    }

    public Player getEnemyPlayer(Player player){
        if (player == player1){
            return player2;
        }
        return player1;
    }

    public void setPlayerInPlay(Player player){
        playerInPlay = player;
    }


    public Move getLastMove(){
        return lastMove;
    }

    public Player getPlayerInPlay(){
        return playerInPlay;
    }

    public GameModel(boolean withAI) {
        this.lastMove = null;
        chessPieces = new Pieces[32];
        board = new Pieces[8][8];
        player1 = new Player(this);
        if(withAI){
            player2 = new AiPlayer(this);
        } else {
            player2 = new Player(this);
        }
        generateKing();
        startingLayout();

        for (Pieces peice: chessPieces){
            board[peice.getCoord().i()][peice.getCoord().j()] = peice;
        }
        //Null Spaces
        for (int i = 0; i < 8; i++) {
            board[i][2] = null;
            board[i][3] = null;
            board[i][4] = null;
            board[i][5] = null;
        }
        player1.setKing(whiteKing);
        player2.setKing(blackKing);
        playerInPlay = player1;
        state = GameState.READY;

        boolean notifyOnEdt = false; // no threads, so false is okay
        propSupport = new SwingPropertyChangeSupport(this, notifyOnEdt);

    }

    public void determineEndGame(){
        determineCheckmateOrStalemate();
        determineInsufficientMaterial();
    }

    private void determineCheckmateOrStalemate(){
        for (Pieces piece: chessPieces){
            if (piece.player == playerInPlay && piece.moves().length>0){
                return;
            }
        }
        if (playerInPlay.getKing().underCheck()){
            state = GameState.CHECKMATE;
            propSupport.firePropertyChange("gameResult", null, GameState.CHECKMATE);
        } else {
            state = GameState.STALEMATE;
            propSupport.firePropertyChange("gameResult", null, GameState.STALEMATE);
        }
    }

    private void determineInsufficientMaterial(){
        if (getChessPieces().length <= 4){
            if (kingVsKing() || kingBishopOrKingKnightVsKing() || kingBishopVsKingBishop()){
                state = GameState.INSUFFICIENT_MATERIAL;
                propSupport.firePropertyChange("gameResult", null, GameState.INSUFFICIENT_MATERIAL);
            }
        }
    }

    private boolean kingVsKing(){
        return getChessPieces().length == 2;
    }

    private boolean kingBishopOrKingKnightVsKing(){
        if (getChessPieces().length == 3){
            for (Pieces piece: chessPieces){
                if (piece instanceof Bishop || piece instanceof Knight) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean kingBishopVsKingBishop(){
        Bishop blackBishop = null;
        Bishop whiteBishop = null;
        for (Pieces piece: chessPieces){
            if (piece instanceof Bishop && piece.player == player2) {
                blackBishop = (Bishop) piece;
            } else if (piece instanceof Bishop && piece.player == player1) {
                whiteBishop = (Bishop) piece;
            }
        }
        return blackBishop != null && whiteBishop != null &&
                blackBishop.getColor() == whiteBishop.getColor();
    }

    public moveType pawnPromotion(){
        Pieces pieceMoved = lastMove.pieceMoved();
        if (pieceMoved instanceof Pawn && ((Pawn) pieceMoved).canPromote()){
            return moveType.PROMOTION;
        }
        return null;
    }

    private void generateKing(){
        //Black King
        chessPieces[0] = new King(new GraphCoord(new iPair(4, 0)),
                player2, this);
        //White King
        chessPieces[1] = new King(new GraphCoord(new iPair(4, 7)),
                player1, this);
        whiteKing = (King) chessPieces[1];
        blackKing = (King) chessPieces[0];
    }
    private void startingLayout() {
        int counter = 2;

        //Black Pawns
        for (int i = 0; i < 8; i++) {
            chessPieces[counter++] = new Pawn(new GraphCoord(new iPair(i, 1)),
                    player2, this);
        }
        //White Pawns
        for (int i = 0; i < 8; i++) {
            chessPieces[counter++] = new Pawn(new GraphCoord(new iPair(i, 6)),
                    player1, this);
        }
        //Black Rook
        chessPieces[counter++] = new Rook(new GraphCoord(new iPair(0, 0)),
                player2, this);
        chessPieces[counter++] = new Rook(new GraphCoord(new iPair(7, 0)),
                player2, this);
        //White Rook
        chessPieces[counter++] = new Rook(new GraphCoord(new iPair(0, 7)),
                player1, this);
        chessPieces[counter++] = new Rook(new GraphCoord(new iPair(7, 7)),
                player1, this);
        //Black Knight
        chessPieces[counter++] = new Knight(new GraphCoord(new iPair(1, 0)),
                player2, this);
        chessPieces[counter++] = new Knight(new GraphCoord(new iPair(6, 0)),
                player2, this);
        //White Knight
        chessPieces[counter++] = new Knight(new GraphCoord(new iPair(1, 7)),
                player1, this);
        chessPieces[counter++] = new Knight(new GraphCoord(new iPair(6, 7)),
                player1, this);
        //Black Bishop
        chessPieces[counter++] = new Bishop(new GraphCoord(new iPair(2, 0)),
                player2, this);
        chessPieces[counter++] = new Bishop(new GraphCoord(new iPair(5, 0)),
                player2, this);
        //White Bishop
        chessPieces[counter++] = new Bishop(new GraphCoord(new iPair(2, 7)),
                player1, this);
        chessPieces[counter++] = new Bishop(new GraphCoord(new iPair(5, 7)),
                player1, this);
        //Black Queen
        chessPieces[counter++] = new Queen(new GraphCoord(new iPair(3, 0)),
                player2, this);
        //White Queen
        chessPieces[counter] = new Queen(new GraphCoord(new iPair(3, 7)),
                player1, this);

    }

    public Pieces[] getChessPieces() {
        return chessPieces;
    }

    public GameState state() {
        return state;
    }

    public void startGame(){
        state = GameState.PLAYING;
    }
    public void undoMove(){
        if( lastMove != null) {
            Move undoTo = lastMove.lastMove();
            playerInPlay.setValidMoves(null);
            if (playerInPlay == player1) {
                playerInPlay = player2;
            } else {
                playerInPlay = player1;
            }
            iPair lastPosition = lastMove.positionFrom().getCoord();
            Pieces pieceMoved = lastMove.pieceMoved();
            pieceMoved.movePiece(pieceMoved, lastPosition.i(), lastPosition.j());
            undoCapturing();
            undoCastling();
            undoPromotion();
            if (lastMove.WasFirstMove()) {
                pieceMoved.setMovedFalse();
            }
            lastMove = undoTo;
            propSupport.firePropertyChange("boardState", null, null);
        }
    }
    private void undoPromotion(){
        if (lastMove.wasPromoted()){
            swapPiece(lastMove.promotedTo(), lastMove.pieceMoved());
        }
    }
    private void undoCapturing(){
        Pieces captured = lastMove.pieceCaptured();
        if (captured != null){
            iPair capturedPosition = captured.pos.getCoord();
            board[capturedPosition.i()][capturedPosition.j()] = captured;
            addPiece(captured);
        }
    }
    private void undoCastling(){
        if (lastMove.wasCastling){
            iPair currentPosition = lastMove.positionTo().getCoord();
            Pieces rook;
            if (currentPosition.i() == 2){
                rook = board[currentPosition.i() + 1][currentPosition.j()];
            } else {
                rook = board[currentPosition.i() - 1][currentPosition.j()];
            }
            rook.movePiece(rook,rook.startPos.getCoord().i(), rook.startPos.getCoord().j());
            rook.setMovedFalse();
        }
    }
    public void swapPiece(Pieces currPiece, Pieces replacement){
        int counter = 0;
        for (Pieces chessPiece: chessPieces){
            if (chessPiece == currPiece) {
                chessPieces[counter] = replacement;
                return;
            }
            counter ++;
        }
    }
    public void removePiece(Pieces piece){
        int counter = 0;
        Pieces[] newChessPieces = new Pieces[chessPieces.length-1];
        for (Pieces chessPiece: chessPieces){
            if (chessPiece != piece) {
                newChessPieces[counter ++]=chessPiece;
            }
        }
        chessPieces = newChessPieces;
    }

    public void addPiece(Pieces piece){
        int counter = 0;
        Pieces[] newChessPieces = new Pieces[chessPieces.length+1];
        for (Pieces chessPiece: chessPieces){
            newChessPieces[counter ++]=chessPiece;
        }
        newChessPieces[counter] = piece;
        chessPieces = newChessPieces;
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(propertyName, listener);
    }
}

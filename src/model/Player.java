package model;

import java.util.Objects;
import model.Board.GraphCoord;
import model.Board.iPair;
import model.GameModel.moveType;

public class Player {
    protected final GameModel model;
    protected Pieces selectedPiece;
    protected GraphCoord[] validMoves;
    protected final Pieces[][] board;
    protected King king;
    protected String color;

    public void setKing(King king){
        this.king = king;
        if(king.pos.getCoord().j()==0){
            color = "BLACK";
        } else if (king.pos.getCoord().j()==7){
            color = "WHITE";
        }
    }
    public String getColor() {
        return color;
    }

    public King getKing() {
        return king;
    }

    public Pieces getSelectedPiece(){
        return selectedPiece;
    }

    public void setValidMoves(GraphCoord[] moves){
        validMoves = moves;
    }

    public GraphCoord[] getValidMoves(){
        return validMoves;
    }
    public Player(GameModel model){
        this.model= model;
        board = model.getBoard();
        selectedPiece = null;
    }

    public moveType tryMove(int row, int col){
        if(selectedPiece != null) {
            for (GraphCoord move : validMoves) {
                if (Objects.equals(move.getCoord(), new iPair(row, col))) {
                    selectedPiece.setPos(row, col);
                    validMoves = null;
                    return processEndTurn();
                }
            }
        }
        return moveType.INVALID;
    }

    private boolean canSelect(int row, int col){
        Pieces selected = board[row][col];
        return selected != null && selected.getPlayer() == model.getPlayerInPlay();
    }

    public moveType processEndTurn(){
        Player enemyPlayer = model.getEnemyPlayer();
        model.setPlayerInPlay(enemyPlayer);
        enemyPlayer.getKing().scanForEnemyChecks();
        model.setLastMove(selectedPiece.getLastMove());
        moveType validPromotion = model.pawnPromotion();
        if(validPromotion!= null){
            return validPromotion;
        }
        selectedPiece = null;
        if (model.getPlayerInPlay() instanceof AiPlayer){
            ((AiPlayer) model.getPlayerInPlay()).makeMove();
            model.setPlayerInPlay(this);
        }
        System.out.println("PLAYER TURN END");
        System.out.println(model.getPlayerInPlay());
        return moveType.VALID;
    }

    public GraphCoord[] makeSelection(int row, int col){
        if (canSelect(row, col)){
            selectedPiece = board[row][col];
            selectedPiece.setValidMoves();
        }
        return validMoves;
    }

    /// ////

    public boolean tryMove(GraphCoord move, Pieces selectedPiece){
        selectedPiece.setPos(move.getCoord().i(), move.getCoord().j());
        validMoves = null;
        return processEndTurnA(selectedPiece);
    }

    public boolean processEndTurnA(Pieces selectedPiece){
        model.setLastMove(selectedPiece.getLastMove());
        return pawnPromotion();
    }

    public boolean pawnPromotion(){
        Pieces pieceMoved = model.getLastMove().pieceMoved();
        return pieceMoved instanceof Pawn && ((Pawn) pieceMoved).canPromote();
    }

}

package model;


import java.util.ArrayList;
import java.util.Objects;
import model.Board.GraphCoord;
import model.Board.iPair;


public class Pawn extends Pieces{

    public Pawn(GraphCoord pos, Player player, GameModel model){
        super(pos, player, model);
        value = 100;
    }

    @Override
    public GraphCoord[] moves() {
        ArrayList<GraphCoord> output = new ArrayList<>();
        iPair coord = pos.getCoord();
        startingMove(coord, output);
        forwardMove(coord, output);
        checkCaptures(coord, output);
        GraphCoord potentialMove = enPassantAttack();
        if (potentialMove != null) {
            output.add(potentialMove);
        }
        return output.toArray(new GraphCoord[0]);
    }
    
    private void startingMove(iPair coord, ArrayList<GraphCoord> output){
        int i = coord.i();
        int j = coord.j() - 2 * direction(player);
        if (!moved && board[i][j]==null &&
                board[i][coord.j()-direction(player)]==null
                && kingNotInCheck( i, j)){
            output.add(startingMove(coord, player));
        }
    }
    
    private void forwardMove(iPair coord, ArrayList<GraphCoord> output){
        int i = coord.i();
        int j = coord.j()-direction(player);
        if (coord.j()-direction(player) >= 0 && coord.j()-direction(player) < 8 &&
                board[i][j]==null &&
                kingNotInCheck( i, j)) {
            output.add(forwardMove(coord, player));
        }
    }
    
    private void checkCaptures(iPair coord, ArrayList<GraphCoord> output){
        if (coord.j()-direction(player) >= 0 && coord.j()-direction(player) < 8) {
            int i = coord.i() + 1;
            int j = coord.j()-direction(player);
            if (coord.i() + 1 < 8 && checkValidMove(coord, i, j)){
                output.add(leftCapture(coord, player));
            }
            i = coord.i() - 1;
            if (coord.i() - 1 >= 0 && checkValidMove(coord, i, j)) {
                output.add(rightCapture(coord, player));
            }

        }
    }
    
    protected static int direction(Player player){
        int direction = 1;
        if (Objects.equals(player.getColor(), "BLACK")){
            direction = -1;
        }
        return direction;
    }
    
    private GraphCoord startingMove(iPair coord, Player player){
        return new GraphCoord(new iPair(coord.i(), coord.j()-2*direction(player)));
    }
    private GraphCoord forwardMove(iPair coord, Player player){
        return new GraphCoord(new iPair(coord.i(), coord.j()- direction(player)));
    }
    protected static GraphCoord leftCapture(iPair coord, Player player){
        return new GraphCoord(new iPair(coord.i() + 1, coord.j()-direction(player)));
    }

    protected static GraphCoord rightCapture(iPair coord, Player player){
        return new GraphCoord(new iPair(coord.i() - 1, coord.j()-direction(player)));
    }

    private GraphCoord enPassantAttack(){
        iPair coord = pos.getCoord();
        if (model.getLastMove() != null) {
            if (coord.i() + 1 >= 0 && coord.i() + 1 < 8) {
                Pieces enemyPawn = board[coord.i() + 1][coord.j()];
                if (enPassantConditions(coord.i() + 1, coord.j())) {
                    return enPassantCapture(enemyPawn);
                }
            }
            if (coord.i() - 1 >= 0 && coord.i() - 1 < 8) {
                Pieces enemyPawn = board[coord.i() - 1][coord.j()];
                if (enPassantConditions(coord.i() - 1,coord.j())) {
                    return enPassantCapture(enemyPawn);
                }
            }
        }
        return null;
    }
    private boolean enPassantConditions(int x, int y){
        return enemyPawnLastMove(x,y) && enemyPawnMovedDoubleTiles();
    }

    private boolean enemyPawnLastMove(int x, int y){
        return model.getLastMove() != null &&
                board[x][y] instanceof Pawn && board[x][y] == model.getLastMove().pieceMoved();
    }

    private boolean enemyPawnMovedDoubleTiles(){
        return Math.abs(model.getLastMove().positionFrom().getCoord().j()-
                model.getLastMove().positionTo().getCoord().j()) == 2;
    }
    
    private GraphCoord enPassantCapture(Pieces enemyPawn){
        iPair enemyPawnCoord = enemyPawn.getPos().getCoord();
        return new GraphCoord(new iPair(enemyPawnCoord.i(), enemyPawnCoord.j() - direction(player)));
    }
    
    protected boolean canPromote(){
        int lastRow = 7;
        if (Objects.equals(player.getColor(), "WHITE")){
            lastRow = 0;
        }
        return pos.getCoord().j() == lastRow;
    }
    protected void promoteTo (Pieces promoted){
        lastMove.setWasPromoted(true);
        lastMove.setPromotedTo(promoted);
        model.swapPiece(this, promoted);
        board[pos.getCoord().i()][pos.getCoord().j()] = promoted;
    }

    public void processPromotion(int promotion){
        Pieces promoted;
        if (canPromote()){
            if (promotion == 1) {
                promoted = new Queen(pos, player, model);
            } else if (promotion == 2) {
                promoted = new Rook(pos, player, model);
            }else if (promotion == 3) {
                promoted = new Bishop(pos, player, model);
            }else {
                promoted = new Knight(pos, player, model);
            }
            promoteTo(promoted);
        }
    }

    @Override
    protected void specialCases(int i, int j){
        if(board[i][j] == null && enPassantConditions(i, j+ Pawn.direction(player))){
            processCapture( i, j + Pawn.direction(player));
        }
    }
    @Override
    protected boolean checkValidMove(iPair coord, int x, int y){
        return board[x][y] != null && board[x][y].player != player && kingNotInCheck(x, y);
    }

}

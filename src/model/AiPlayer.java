package model;

import java.util.ArrayList;

import model.Board.GraphCoord;
import model.Board.iPair;

public class AiPlayer extends Player{

    public AiPlayer(GameModel model) {
        super(model);
    }

    public void makeMove() {
        int bestScore = Integer.MIN_VALUE;
        Pieces bestPieceToMove = null;
        GraphCoord bestMove = null;
        for (Pieces pieces: model.getChessPieces()) {
            if (pieces.getPlayer() == this && !pieces.isCaptured()) {
                for (GraphCoord moves : pieces.moves()) {
                    this.moveSimulation(moves, pieces);
                    int moveScore = evaluateBestMove(2, this, true);
                    model.undoMove();
                    if (moveScore > bestScore) {
                        bestScore = moveScore;
                        bestMove = moves;
                        bestPieceToMove = pieces;
                    }
                }
            }
        }
        tryMove(bestMove, bestPieceToMove);
        processEndTerm(bestPieceToMove);
    }

    private int evaluateBestMove(int depth, Player currentPlayer, boolean isMaximizing){
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (depth == 0){
            return evaluateMove(currentPlayer);
        }
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.getPlayer() == currentPlayer && !pieces.isCaptured()){
                for(GraphCoord moves: pieces.moves()){
                    currentPlayer.moveSimulation(moves, pieces);
                    int score = evaluateBestMove(depth - 1, model.getEnemyPlayer(currentPlayer), !isMaximizing);
                        if (isMaximizing) {
                            bestScore = Math.max(bestScore, score);
                        } else {
                            bestScore = Math.min(bestScore, score);
                        }
                    model.undoMove();
                }
            }
        }
        return bestScore;
    }
    private int evaluateMove(Player currentPlayer){
        
        iPair material = countMaterial(currentPlayer);
        int playerPoints = material.i();
        int enemyPoints = material.j();
        if (currentPlayer.getKing().underCheck()) {
            playerPoints -= 50;
        }
        if (model.getEnemyPlayer(currentPlayer).getKing().underCheck()) {
        playerPoints += 50;
        }
        
        if (model.getLastMove() != null && model.getLastMove().pieceCaptured() != null) {
        Pieces moved = model.getLastMove().pieceMoved();
        Pieces captured = model.getLastMove().pieceCaptured();
        int tradeValue = captured.getValue() - moved.getValue();
        playerPoints += tradeValue;
        }
        
        for (Pieces piece : model.getChessPieces()) {
        if (piece.getPlayer() == currentPlayer && !piece.isCaptured()) {
            iPair pos = piece.getPos().getCoord();
            if ((pos.i() == 3 || pos.i() == 4) && (pos.j() == 3 || pos.j() == 4)) {
                playerPoints += 20;
            }
        }
    }
    
    return playerPoints - enemyPoints;
    }

    private iPair countMaterial(Player currentPlayer){
        int playerPoints = 0;
        int enemyPoints = 0;
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.player == currentPlayer && !pieces.isCaptured()){
                playerPoints += pieces.getValue();
            } else if (!pieces.isCaptured()){
                enemyPoints += pieces.getValue();
            }
        }
        return new iPair(playerPoints,enemyPoints);
    }

    public void processEndTerm(Pieces selectedPiece){
        model.setLastMove(selectedPiece.getLastMove());
        Player enemyPlayer = model.getEnemyPlayer();
        model.setPlayerInPlay(enemyPlayer);
        model.getEnemyPlayer(this).getKing().scanForEnemyChecks();
        pawnPromotion();
    }
}


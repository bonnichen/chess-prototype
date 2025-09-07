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
                    int moveScore = evaluateBestMove(3, this, true);
                    model.undoMove();
                    if (Math.max(moveScore, bestScore) != bestScore) {
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
            if (pieces.getPlayer() == currentPlayer){
                for(GraphCoord moves: pieces.moves()){
                    currentPlayer.moveSimulation(moves, pieces);
                    int score = evaluateBestMove(depth - 1, model.getEnemyPlayer(currentPlayer), !isMaximizing);
                        if (isMaximizing) {
                            bestScore = Math.max(bestScore, score);
                        } else {
                            bestScore = Math.min(bestScore, score);
                        }
                    // if (currentPlayer.tryMove(moves, pieces)){
                    //     Pawn pawn = (Pawn) pieces;
                    //     for (int promoType = 1; promoType <= 4; promoType++) {
                    //         pawn.processPromotion(promoType);
                    //         int score = evaluateBestMove(depth - 1, model.getEnemyPlayer(currentPlayer), !isMaximizing);
                    //         if (isMaximizing) {
                    //             bestScore = Math.max(bestScore, score);
                    //         } else {
                    //             bestScore = Math.min(bestScore, score);
                    //         }
                    //     }
                    // } else {
                    //     int score = evaluateBestMove(depth - 1, model.getEnemyPlayer(currentPlayer), !isMaximizing);
                    //     if (isMaximizing) {
                    //         bestScore = Math.max(bestScore, score);
                    //     } else {
                    //         bestScore = Math.min(bestScore, score);
                    //     }
                    // }
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

        for (Pieces piece : model.getChessPieces()) {
        if (piece.getPlayer() == currentPlayer) {
            iPair pos = piece.getPos().getCoord();
            if ((pos.i() == 3 || pos.i() == 4) && (pos.j() == 3 || pos.j() == 4)) {
                playerPoints += 2;
            }
        }
    }

    return playerPoints - enemyPoints;
    }

    private iPair countMaterial(Player currentPlayer){
        int playerPoints = 0;
        int enemyPoints = 0;
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.player == currentPlayer){
                playerPoints += pieces.getValue();
            } else {
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


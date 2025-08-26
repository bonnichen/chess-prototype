package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.getPlayer() == this) {
                for (GraphCoord moves : pieces.moves()) {
                    int moveScore = evaluateBestMove(3,this, moves);
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

    private int evaluateBestMove(int depth, Player currentPlayer, GraphCoord move){
        Map<GraphCoord, Integer> scores = new HashMap<>();
        int bestScore = Integer.MIN_VALUE;
        int totalScore = 0;
//
//        if (totalScore < 0 && currentPlayer == this){
//            return totalScore;
//        } else if (totalScore > 0 && currentPlayer != this){
//            return - totalScore;
//        }
        if (depth == 0){
            return evaluateMove(currentPlayer, move);
        }
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.getPlayer() == currentPlayer){
                for(GraphCoord moves: pieces.moves()){
                    if (currentPlayer.tryMove(moves, pieces)){
                        Pawn pawn = (Pawn) pieces;
                        pawn.processPromotion(1);
                        totalScore += evaluateBestMove(depth -1, model.getEnemyPlayer(currentPlayer), moves);
                        pawn.processPromotion(2);
                        totalScore += evaluateBestMove(depth -1, model.getEnemyPlayer(currentPlayer), moves);
                        pawn.processPromotion(3);
                        totalScore += evaluateBestMove(depth -1, model.getEnemyPlayer(currentPlayer), moves);
                        pawn.processPromotion(4);
                    } else {
                        totalScore += evaluateBestMove(depth - 1, model.getEnemyPlayer(currentPlayer), moves);
                        scores.put(moves, totalScore);
                    }
                    model.undoMove();
                }
                for(Entry<GraphCoord, Integer> moves: scores.entrySet()){
                    if (moves.getValue() > bestScore) {
                        bestScore = moves.getValue();
                    }
                }
            }
        }
        return bestScore;
    }
    private int evaluateMove(Player currentPlayer, GraphCoord move){
        iPair material = countMaterial(currentPlayer);
        int playerPoints = material.i();
        int enemyPoints = material.j();

        if ((move.getCoord().i() == 3 || move.getCoord().i() == 4) &&
                (move.getCoord().j() == 3 || move.getCoord().j() == 4)) {
            playerPoints += 2;
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

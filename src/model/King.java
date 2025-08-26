package model;

import java.util.ArrayList;
import model.Board.GraphCoord;
import model.Board.iPair;

public class King extends Pieces{

    public King(GraphCoord pos, Player player, GameModel model){
        super(pos, player, model);
    }

    @Override
    public GraphCoord[] moves() {
        ArrayList<GraphCoord> output = new ArrayList<>();
        iPair coord = pos.getCoord();
        singleTileMovement(coord, output, 1,0);
        singleTileMovement(coord, output, -1,0);
        singleTileMovement(coord, output, 0,1);
        singleTileMovement(coord, output, 0,-1);
        singleTileMovement(coord, output, 1,1);
        singleTileMovement(coord, output, -1,1);
        singleTileMovement(coord, output, -1,-1);
        singleTileMovement(coord, output, 1,-1);
        kingSideCastle(output);
        queenSideCastle(output);
        return output.toArray(new GraphCoord[0]);
    }

    private void singleTileMovement(iPair coord, ArrayList<GraphCoord> output, int x, int y){
        if (checkValidMove(coord, x, y) && !nextMoveUnderCheck(x,y)){
            output.add(new GraphCoord(new iPair(coord.i() + x, coord.j() + y)));
        }
    }

    private void kingSideCastle(ArrayList<GraphCoord> output){
        Pieces rook = board[7][getLastRow()];
        iPair[] path = new iPair[]{new iPair(5,getLastRow()),
                new iPair(6,getLastRow())};
        if (!moved && rook instanceof Rook && !rook.moved &&
                noPieceBetween(path) && pathNotUnderAttack(path)){
            output.add(new GraphCoord(new iPair(6, getLastRow())));
        }
    }

    private void queenSideCastle(ArrayList<GraphCoord> output){
        Pieces rook = board[0][getLastRow()];
        iPair[] path = new iPair[]{new iPair(1,getLastRow()),new iPair(2,getLastRow()),
                new iPair(3,getLastRow())};
        if (!moved && rook instanceof Rook && !rook.moved &&
                noPieceBetween(path)){
            path = new iPair[]{new iPair(2,getLastRow()), new iPair(3,getLastRow())};
            if(pathNotUnderAttack(path)) {
                output.add(new GraphCoord(new iPair(2, getLastRow())));
            }
        }
    }

    private boolean pathNotUnderAttack(iPair[] path){
        for (iPair tile: path){
            int i = tile.i() - pos.getCoord().i();
            int j = tile.j() - pos.getCoord().j();
            if(nextMoveUnderCheck(i, j)){
                return false;
            }
        }
        return true;
    }

    private boolean noPieceBetween(iPair[] path){
        for (iPair tile: path){
            if(board[tile.i()][tile.j()] != null){
                return false;
            }
        }
        return true;
    }

    private boolean nextMoveUnderCheck(int x, int y){
        iPair pos = this.pos.getCoord();
        int nextPosI = pos.i() + x;
        int nextPosJ = pos.j() + y;
        Pieces replaced = movePiece(this, nextPosI, nextPosJ);
        boolean output = underCheck();
        board[nextPosI][nextPosJ] = replaced;
        board[pos.i()][pos.j()] = this;
        this.pos.setCoord(pos);
        return output;
    }


    public boolean underCheck(){
        return scanForEnemyChecks().length > 0;
    }

    public GraphCoord[] scanForEnemyChecks(){
        ArrayList<GraphCoord> pieces = new ArrayList<>();
        scanKnightAttacks(pieces);
        scanStriaghtLineAttacks(pieces);
        scanDiagonalAttacks(pieces);
        scanPawnAttacks(pieces);
        return pieces.toArray(new GraphCoord[0]);
    }

    private void scanKnightAttacks(ArrayList<GraphCoord> pieces){
        iPair coord = pos.getCoord();
        GraphCoord[] knightMoves = allLAttacks(coord);
        for (GraphCoord tile: knightMoves){
            int i = tile.getCoord().i();
            int j = tile.getCoord().j();
            if (i >= 0 && i < 8 && j >= 0 && j < 8) {
                iPair enemyPos = tile.getCoord();
                Pieces enemy = board[enemyPos.i()][enemyPos.j()];
                if (enemy instanceof Knight && enemy.getPlayer() != player) {
                    pieces.add(tile);
                }
            }
        }
    }

    private GraphCoord[] allLAttacks(iPair coord) {
        return new GraphCoord[]{
                new GraphCoord(new iPair(coord.i() + 2, coord.j() + 1)),
                new GraphCoord(new iPair(coord.i() + 2, coord.j() - 1)),
                new GraphCoord(new iPair(coord.i() - 2, coord.j() + 1)),
                new GraphCoord(new iPair(coord.i() - 2, coord.j() - 1)),
                new GraphCoord(new iPair(coord.i() + 1, coord.j() + 2)),
                new GraphCoord(new iPair(coord.i() - 1, coord.j() + 2)),
                new GraphCoord(new iPair(coord.i() + 1, coord.j() - 2)),
                new GraphCoord(new iPair(coord.i() - 1, coord.j() - 2))
        };
    }

    private void scanStriaghtLineAttacks (ArrayList<GraphCoord> pieces){
        iPair coord = pos.getCoord();
        Pieces enemy = firstHorizontalEnemy(coord, board, 1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstHorizontalEnemy(coord, board, -1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstVerticalEnemy(coord, board, 1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstVerticalEnemy(coord, board, -1);
        if(enemy != null){pieces.add(enemy.pos);}
    }

    private Pieces firstHorizontalEnemy(iPair coord, Pieces[][] board, int x){
        int move = 0;
        move = move + x;
        while (coord.i() + move < 8 && coord.i() + move >= 0){
            iPair enemyPos = Rook.checkHorizontal(coord,move).getCoord();
            Pieces enemy = board[enemyPos.i()][enemyPos.j()];
            if (enemy != null){
                if ((enemy instanceof Queen && enemy.getPlayer()!= player)
                        || (enemy instanceof Rook && enemy.getPlayer()!= player)){
                    return board[enemyPos.i()][enemyPos.j()];
                }
                break;
            }
            move = move + x;
        }
        return null;
    }

    private Pieces firstVerticalEnemy (iPair coord, Pieces[][] board, int y){
        int move = 0;
        move = move + y;
        while (coord.j() + move < 8 && coord.j() + move >= 0){
            iPair enemyPos = Rook.checkVertical(coord,move).getCoord();
            Pieces enemy = board[enemyPos.i()][enemyPos.j()];
            if (enemy != null){
                if ((enemy instanceof Queen && enemy.getPlayer()!= player)
                        || (enemy instanceof Rook && enemy.getPlayer()!= player)){
                    return board[enemyPos.i()][enemyPos.j()];
                }
                break;
            }
            move = move + y;
        }
        return null;
    }


    private void scanDiagonalAttacks(ArrayList<GraphCoord> pieces){
        iPair coord = pos.getCoord();
        Pieces enemy = firstDiagonalEnemy(coord, board, 1, 1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstDiagonalEnemy(coord, board, -1, 1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstDiagonalEnemy(coord, board, 1, -1);
        if(enemy != null){pieces.add(enemy.pos);}
        enemy = firstDiagonalEnemy(coord, board, -1, -1);
        if(enemy != null){pieces.add(enemy.pos);}
    }

    private Pieces firstDiagonalEnemy(iPair coord, Pieces[][] board, int x, int y){
        int moveX = 0;
        moveX = moveX + x;
        int moveY = 0;
        moveY = moveY + y;
        while (coord.i() + moveX < 8 && coord.i() + moveX >= 0 &&
                coord.j() + moveY < 8 && coord.j() + moveY >= 0){
            iPair enemyPos = Bishop.checkDiagonal(coord, moveX, moveY).getCoord();
            Pieces enemy = board[enemyPos.i()][enemyPos.j()];
            if (enemy != null){
                if ((enemy instanceof Queen && enemy.getPlayer()!= player)
                        || (enemy instanceof Bishop && enemy.getPlayer()!= player)){
                    return board[enemyPos.i()][enemyPos.j()];
                }
                break;
            }
            moveX = moveX + x;
            moveY = moveY + y;
        }
        return null;
    }

    private void scanPawnAttacks(ArrayList<GraphCoord> pieces) {
        iPair enemyPos = Pawn.leftCapture(pos.getCoord(), player).getCoord();
        if (enemyPos.i() < 8 && enemyPos.i() >= 0 && enemyPos.j() < 8 && enemyPos.j() >= 0) {
            Pieces enemy = board[enemyPos.i()][enemyPos.j()];
            if (enemy instanceof Pawn && enemy.getPlayer() != player) {
                pieces.add(enemy.pos);
            }
        }
        enemyPos = Pawn.rightCapture(pos.getCoord(), player).getCoord();
        if (enemyPos.i() < 8 && enemyPos.i() >= 0 && enemyPos.j() < 8 && enemyPos.j() >= 0) {
            Pieces enemy = board[enemyPos.i()][enemyPos.j()];
            if (enemy instanceof Pawn && enemy.getPlayer() != player) {
                pieces.add(enemy.pos);
            }
        }
    }

    private boolean noEnemyKingInVicinity(iPair coord, int x, int y){
        iPair checkCoord = new iPair(coord.i() + x, coord.j() + y);
        return (noKingOnTile(checkCoord, 1,1) &&
                noKingOnTile(checkCoord,0,1) &&
                noKingOnTile(checkCoord,1,0) &&
                noKingOnTile(checkCoord,-1,0) &&
                noKingOnTile(checkCoord,0,-1) &&
                noKingOnTile(checkCoord,-1,-1) &&
                noKingOnTile(checkCoord,-1,1) &&
                noKingOnTile(checkCoord,1,-1));

    }

    private boolean noKingOnTile(iPair coord, int x, int y) {
        if (coord.i() + x >= 0 && coord.i() + x < 8 &&
                coord.j() + y >= 0 && coord.j() + y < 8) {
            iPair checkCoord = new iPair(coord.i() + x, coord.j() + y);
            if (board[checkCoord.i()][checkCoord.j()] != null &&
                    board[checkCoord.i()][checkCoord.j()].getPlayer() != player &&
                    board[checkCoord.i()][checkCoord.j()] instanceof King) {
                return false;
            }
        }
        return true;
    }


    @Override
    protected void specialCases(int i, int j){
        if (!moved && (i - pos.getCoord().i())==2){
            Pieces rook = board[7][getLastRow()];
            movePiece(rook, 5,getLastRow());
            lastMove.setWasCastling(true);
        } else if (!moved && (i - pos.getCoord().i()) <= -2){
            Pieces rook = board[0][getLastRow()];
            movePiece(this, 2,getLastRow());
            movePiece(rook, 3,getLastRow());
            lastMove.setWasCastling(true);
        }
    }

    @Override
    protected boolean checkValidMove(iPair coord, int x, int y) {
        return coord.i() + x >= 0 && coord.i() + x < 8 &&
                coord.j() + y >= 0 && coord.j() + y < 8 &&
                (board[coord.i() + x][coord.j() + y] == null ||
                        board[coord.i() + x][coord.j() + y].player != player)
                && noEnemyKingInVicinity(coord,x,y);
    }
}

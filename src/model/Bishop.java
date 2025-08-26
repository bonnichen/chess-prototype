package model;

import java.util.ArrayList;
import model.Board.GraphCoord;
import model.Board.iPair;

public class Bishop extends Pieces{
    private final int color;

    public int getColor(){
        return color;
    }
    public Bishop(GraphCoord pos, Player player, GameModel model){
        super(pos, player, model);
        if ((pos.getCoord().i() + pos.getCoord().j()) % 2 == 0){
            color = 1;
        } else {
            color = 2;
        }
        value = 300;
    }

    @Override
    public GraphCoord[] moves() {
        ArrayList<GraphCoord> output = new ArrayList<>();
        iPair coord = pos.getCoord();

        diagonalMoves(output, coord, 1, 1);
        diagonalMoves(output, coord, 1, -1);
        diagonalMoves(output, coord, -1, 1);
        diagonalMoves(output, coord, -1, -1);

        return output.toArray(new GraphCoord[0]);
    }
    private void diagonalMoves(ArrayList<GraphCoord> output, iPair coord, int x, int y){
        int movex = x;
        int movey = y;
        while(checkValidMove(coord, movex, movey)){
            if (kingNotInCheck(coord.i() + movex, coord.j() + movey)) {
                output.add(checkDiagonal(coord,movex,movey));
            }
            if (checkCapture(coord, movex, movey)){
                break;
            }
            movex += x;
            movey += y;
        }
    }
    protected static GraphCoord checkDiagonal(iPair coord, int x, int y){
        return new GraphCoord(new iPair(coord.i() + x, coord.j() + y));
    }
}

package model;

import java.util.ArrayList;
import model.Board.GraphCoord;
import model.Board.iPair;

public class Knight extends Pieces{

    public Knight(GraphCoord pos, Player player, GameModel model){
        super(pos, player, model);
        value = 300;
    }

    @Override
    public GraphCoord[] moves() {
        ArrayList<GraphCoord> output = new ArrayList<>();
        iPair coord = pos.getCoord();
        lMovement(coord, output, 2, 1);
        lMovement(coord, output, 2, -1);
        lMovement(coord, output, -2, 1);
        lMovement(coord, output, -2, -1);
        lMovement(coord, output, 1, 2);
        lMovement(coord, output, -1, 2);
        lMovement(coord, output, 1, -2);
        lMovement(coord, output, -1, -2);
        return output.toArray(new GraphCoord[0]);
    }

    private void lMovement(iPair coord, ArrayList<GraphCoord> output, int x, int y){
        if (checkValidMove(coord, x, y)){
            output.add(new GraphCoord(new iPair(coord.i() + x, coord.j() + y)));
        }
    }

    @Override
    protected boolean checkValidMove(iPair coord, int x, int y){
        return coord.i() + x >= 0 && coord.i() + x < 8 &&
                coord.j() + y >= 0 && coord.j() + y < 8 &&
                (board[coord.i() + x][coord.j() + y]==null ||
                        board[coord.i() + x][coord.j() + y].getPlayer() != player)
                && kingNotInCheck(coord.i() + x, coord.j() + y);
    }

}

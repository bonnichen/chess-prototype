package model;

import java.util.ArrayList;
import model.Board.GraphCoord;
import model.Board.iPair;

public class Rook extends Pieces{


    public Rook(GraphCoord pos, Player player,GameModel model){
        super(pos, player, model);
        value = 500;
    }

    @Override
    public GraphCoord[] moves() {
        ArrayList<GraphCoord> output = new ArrayList<>();
        iPair coord = pos.getCoord();

        //Horizontal movement
        horizontalMoves(output, coord,1);
        horizontalMoves(output, coord,-1);

        //Vertical movement
        verticalMoves(output, coord, 1);
        verticalMoves(output, coord, -1);

        return output.toArray(new GraphCoord[0]);
    }

    private void horizontalMoves(ArrayList<GraphCoord> output, iPair coord, int move){
        int counter = move;
        while(checkValidMove(coord, counter, 0)){
            if (kingNotInCheck(coord.i() + counter, coord.j())) {
                output.add(checkHorizontal(coord, counter));
            }
            if (checkCapture(coord, counter, 0)){
                break;
            }
            counter += move;
        }
    }

    private void verticalMoves (ArrayList<GraphCoord> output, iPair coord, int move){
        int counter = move;
        while(checkValidMove(coord, 0, counter)){
            if (kingNotInCheck( coord.i(), coord.j() + counter)) {
                output.add(checkVertical(coord,counter));
            }
            if (checkCapture(coord, 0, counter)){
                break;
            }
            counter += move;
        }
    }

    protected static GraphCoord checkHorizontal(iPair coord, int move){
        return new GraphCoord(new iPair(coord.i() + move, coord.j()));
    }

    protected static GraphCoord checkVertical(iPair coord, int move){
        return new GraphCoord(new iPair(coord.i(), coord.j() + move));
    }


}

package model;

import static model.Bishop.checkDiagonal;
import static model.Rook.checkHorizontal;
import static model.Rook.checkVertical;

import java.util.ArrayList;
import model.Board.GraphCoord;
import model.Board.iPair;

public class Queen extends Pieces{

    public Queen(GraphCoord pos, Player player, GameModel model) {
        super(pos, player, model);
        value = 900;
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

        //Diagonal movement
        diagonalMoves(output, coord, 1, 1);
        diagonalMoves(output, coord, 1, -1);
        diagonalMoves(output, coord, -1, 1);
        diagonalMoves(output, coord, -1, -1);

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
            if (kingNotInCheck(coord.i(), coord.j() + counter)) {
                output.add(checkVertical(coord,counter));
            }
            if (checkCapture(coord, 0, counter)){
                break;
            }
            counter += move;
        }
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
}

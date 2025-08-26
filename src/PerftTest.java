import model.Board.GraphCoord;
import model.GameModel;
import model.Pawn;
import model.Pieces;
import model.Player;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class PerftTest {

    @Test
    public void perftTest1(){
       assertEquals(20, perft(1));
    }

    @Test
    public void perftTest2(){
        assertEquals(400, perft(2));
    }

    @Test
    public void perftTest3(){
        assertEquals(8902, perft(3));
    }


    @Test
    public void perftTest4(){
        assertEquals(197281, perft(4));
    }

    @Test
    public void perftTest5(){
        assertEquals(4865609, perft(5));
    }

    @Test
    public void perftTest6(){
        assertEquals(119060324, perft(6));
    }

    @Test
    public void perftTest7(){
        assertEquals(3195901860L, perft(7));
    }


    private long perft(int depth){
        GameModel model = new GameModel(false);
        long start = System.nanoTime();
        long result = perftTest(model, model.getPlayerInPlay(), depth);
        long end = System.nanoTime();
        double seconds = (end - start)/ 1_000_000_000.0;
        System.out.println("Depth " + depth + ": " + result);
        System.out.println("Time " + seconds + "sec");
        return result;
    }

    private long perftTest(GameModel model, Player currentPlayer, int depth){
        long totalmoves = 0;
        if (depth == 0){
            return 1;
        }
        for (Pieces pieces: model.getChessPieces()){
            if (pieces.getPlayer() == currentPlayer){
                for(GraphCoord moves: pieces.moves()){
                    if (currentPlayer.tryMove(moves, pieces)){
                        Pawn pawn = (Pawn) pieces;
                        pawn.processPromotion(1);
                        totalmoves += perftTest(model, model.getEnemyPlayer(currentPlayer), depth -1);
                        pawn.processPromotion(2);
                        totalmoves += perftTest(model, model.getEnemyPlayer(currentPlayer), depth -1);
                        pawn.processPromotion(3);
                        totalmoves += perftTest(model, model.getEnemyPlayer(currentPlayer), depth -1);
                        pawn.processPromotion(4);
                    } else {
                        totalmoves += perftTest(model, model.getEnemyPlayer(currentPlayer), depth - 1);
                    }
                    model.undoMove();
                }
            }
        }
        return totalmoves;
    }
}

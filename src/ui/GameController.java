package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;
import model.AiPlayer;
import model.Board.GraphCoord;
import model.GameModel;
import model.GameModel.moveType;
import model.King;
import model.Pawn;
import model.Player;

public class GameController extends MouseAdapter{



    public enum GameState {MENU, PLAYING, PROMOTION, CHECKMATE,
        STALEMATE, DRAW, GAMEOVER}
    private GameState state;
    private final GameModel model;
    private final GameBoard gameBoard;
    private Player playerInPlay;

    protected SwingPropertyChangeSupport propSupport;

    public GameController(GameModel model, GameBoard gameBoard){
        this.model = model;
        this.gameBoard = gameBoard;
        state = GameState.PLAYING;

        boolean notifyOnEdt = true;
        playerInPlay = model.getPlayerInPlay();

        propSupport = new SwingPropertyChangeSupport(this, notifyOnEdt);

    }







    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(model.getPlayerInPlay());
        gameBoard.removeHighlights();
        int row = e.getX()/75;
        int col = e.getY()/75;
        GraphCoord[] validMoves = model.getPlayerInPlay().makeSelection(row,col);
        if (state == GameState.PLAYING && model.getPlayerInPlay().getValidMoves()!=null
        && ! (model.getPlayerInPlay() instanceof AiPlayer)){
            propSupport.firePropertyChange("boardState", null, null);
            gameBoard.setValidMoves(validMoves);
            moveType moveType = model.getPlayerInPlay().tryMove(row, col);
            if (moveType == GameModel.moveType.VALID){
                gameBoard.removeHighlights();
                model.determineEndGame();
            }
            if (moveType == GameModel.moveType.PROMOTION){
                state = GameState.PROMOTION;
                gameBoard.showPromotionScreen(playerInPlay);
            }
//            if (moveType != null && model.getPlayerInPlay() instanceof AiPlayer){
//                ((AiPlayer) model.getPlayerInPlay()).makeMove();
//            }

        } else if (state == GameState.PROMOTION){
            promotionSelect(row, col);
            }
        }
    private void promotionSelect(int row, int col){
        assert model.getLastMove().pieceMoved() instanceof Pawn;
        Pawn pawn = (Pawn) model.getLastMove().pieceMoved();
        if(row == 3 && col == 3){
            processPromotion(pawn,1);
        } else if (row == 3 && col == 4){
            processPromotion(pawn,2);
        }else if (row == 4 && col == 3){
            processPromotion(pawn,3);
        }else if (row == 4 && col == 4){
            processPromotion(pawn,4);
        }
        King enemyKing = model.getEnemyPlayer().getKing();
        gameBoard.setUnderCheck(enemyKing.scanForEnemyChecks());
    }

    private void processPromotion(Pawn pawn, int promo){
        pawn.processPromotion(promo);
        gameBoard.removePromotionScreen();
        state = GameState.PLAYING;
    }


    public void processMultiplayerGame() {
        if (state ==GameState.MENU){
            state = GameState.PLAYING;
        }
    }

    public void setModel(GameModel newModel) {
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(propertyName, listener);
    }
}


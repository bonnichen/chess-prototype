package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import model.GameModel;
import model.GameModel.GameState;
import model.Player;

public class GameFrame extends JFrame implements PropertyChangeListener{
    private GameBoard gameBoard;
    private GameModel model;
    private final CardLayout cards;
    private final JPanel basePanel;
    public GameFrame(){
        super("Chess");


//        setPreferredSize(new Dimension(600,600));
        cards = new CardLayout();
        basePanel = new JPanel();
        basePanel.setLayout(cards);
        basePanel.setBorder(null);
        basePanel.setOpaque(true);
        basePanel.setBackground(Color.WHITE);


        MainMenu mainMenu = new MainMenu(new MainMenuOptions() {
            @Override
            public void playMultiplayer() {
                startGame(false);
            }

            @Override
            public void playSingleplayer() {
                startGame(true);
        }});

        basePanel.add(mainMenu, "MENU");
        add(basePanel, BorderLayout.NORTH);

        JButton undoButton = new JButton("UNDO");
        undoButton.addActionListener(e -> model.undoMove());
        add(undoButton, BorderLayout.SOUTH);
        cards.show(basePanel, "MENU");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame(boolean withAi){
        model = new GameModel(withAi);
        gameBoard = new GameBoard(model);
        model.addPropertyChangeListener("gameResult", this);
        JPanel chessPanel = new JPanel();
        basePanel.add(chessPanel, "GAME");
        chessPanel.add(gameBoard);
        cards.show(basePanel, "GAME");
    }

    private void showWinMessage(Player player) {
        JOptionPane.showMessageDialog(this, player.getColor() + " wins by Checkmate");
    }

    private void showDrawMessage(String condition) {
        JOptionPane.showMessageDialog(this, "Draw by " + condition);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("gameResult".equals(evt.getPropertyName())) {
            if (model.state() == GameState.CHECKMATE) {
                showWinMessage(model.getEnemyPlayer());
            } else if (model.state() == GameState.STALEMATE) {
                showDrawMessage("Stalemate");
            } else if (model.state() == GameState.INSUFFICIENT_MATERIAL){
                showDrawMessage("Insufficient Material");
            }
            cards.show(basePanel, "MENU");

        }
    }
}

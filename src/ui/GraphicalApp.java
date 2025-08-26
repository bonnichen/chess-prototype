package ui;

import javax.swing.SwingUtilities;

public class GraphicalApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });


    }
}

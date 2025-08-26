package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

    JButton multiplayerButton = new JButton("Multiplayer");
    JButton singlePlayerButton = new JButton("SinglePlayer");
    public MainMenu (MainMenuOptions option){
        setPreferredSize(new Dimension(600,600));
        setBounds(220, 220, 160, 160);
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Chess");
        title.setFont(new Font("Serif", Font.BOLD, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.setAlignmentX(Component.CENTER_ALIGNMENT);
        options.setBackground(Color.WHITE);

        multiplayerButton.setMaximumSize(new Dimension(200, 50));
        multiplayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        singlePlayerButton.setMaximumSize(new Dimension(200, 50));
        singlePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        options.add(multiplayerButton);
        options.add(Box.createRigidArea(new Dimension(0, 10)));
        options.add(singlePlayerButton);
        add(title);
        add(options);
        multiplayerButton.addActionListener(e -> option.playMultiplayer());
        singlePlayerButton.addActionListener(e-> option.playSingleplayer());
    }

//    private void paintMenu(Graphics g){
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setFont(new Font("serif",Font.BOLD,100));
//        g2.drawString("Chess", 175, 200);
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        paintMenu(g);
//    }


}

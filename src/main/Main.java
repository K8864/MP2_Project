package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        //Window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Test");



        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}
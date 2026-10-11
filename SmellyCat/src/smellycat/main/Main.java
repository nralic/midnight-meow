/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smellycat.main;

import javax.swing.JFrame;
import smellycat.view.GamePanel;

/**
 *
 * @author niki
 */
public class Main {

    public static void main(String[] args) {

        int boardWidth = 460;
        int boardHeight = 640;

        JFrame frame = new JFrame("Smelly cat");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel(boardWidth, boardHeight);
        frame.add(gamePanel);
        frame.pack();

        frame.setVisible(true);

        gamePanel.requestFocusInWindow();

    }

}

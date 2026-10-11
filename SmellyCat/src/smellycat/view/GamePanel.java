/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smellycat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import smellycat.components.Cat;

/**
 *
 * @author niki
 */
public class GamePanel extends JPanel implements KeyListener {

    private int width;
    private int height;

    //private Image background;
    private Cat cat;

    private Timer gameLoop;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(this);
        
        //background = new ImageIcon(location)
        cat = new Cat(width, height);

        gameLoop = new Timer(1000 / 60, (ActionEvent e) -> {
            if (!gameOver()) {
                move();
                repaint();
            }
        });
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // background
        // g.drawImage(background, 0, 0, width, height, null);
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        // cat
        // g.drawImage....
        g.setColor(Color.orange);
        g.fillOval(cat.getX(), cat.getY(), cat.getSize(), cat.getSize());
    }

    private void move() {
        cat.move();
    }

    private boolean gameOver() {
        if (cat.getY() + cat.getSize() >= height) {
            gameLoop.stop();
            return true;
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
             cat.setVelocityY(-10);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}

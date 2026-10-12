/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smellycat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import smellycat.components.Cat;
import smellycat.components.Pipe;
import smellycat.enums.GameStatus;
import smellycat.storage.ScoreStorage;

/**
 *
 * @author niki
 */
public class GamePanel extends JPanel implements KeyListener {

    private final int width;
    private final int height;

    //private Image background;
    private Cat cat;

    private final Timer gameLoop;
    private final Timer pipeTimer;

    private final List<Pipe> pipes;

    GameStatus status;

    private int score;
    private int bestScore;

    private JButton btnRestart;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(this);

        status = GameStatus.START;

        score = 0;

        //background = new ImageIcon(location)
        cat = new Cat(width, height);

        pipes = new ArrayList<>();

        gameLoop = new Timer(1000 / 60, (ActionEvent e) -> {
            move();
            gameOver();
            repaint();
        });

        pipeTimer = new Timer(2500, (ActionEvent e) -> {
            placePipes();
        });

        addRestartButton();

        bestScore = ScoreStorage.loadBestScore();
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

        switch (status) {
            case START -> {
                g.setColor(Color.white);
                String text = "SMELLY CAT";
                g.setFont(new Font("Monospaced", Font.BOLD, 32));
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(text)) / 2;
                int y = (int) (height / 2.5);
                g.drawString(text, x, y);
                text = "PRESS SPACE TO START";
                g.setFont(new Font("Monospaced", Font.BOLD, 18));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = (int) (height / 2.5 + 50);
                g.drawString(text, x, y);
                text = "Oh I wish I could, but I don't want to.";
                g.setFont(new Font("Monospaced", Font.BOLD, 13));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = height - 100;
                g.drawString(text, x, y);
            }
            case PLAY -> {
                // cat
                // g.drawImage....

                g.setColor(Color.orange);
                g.fillOval(cat.getX(), cat.getY(), cat.getSize(), cat.getSize());
                // pipes
                //...
                g.setColor(Color.pink);
                for (Pipe p : pipes) {
                    // top
                    g.fillRect(p.getX(), 0, p.getWidth(), p.getTopHeight());
                    // bottom
                    g.fillRect(p.getX(), p.getBottomY(), p.getWidth(), this.getHeight() - p.getBottomY());
                }

                // score
                g.setColor(Color.white);
                String text = String.valueOf(score);
                g.setFont(new Font("Monospaced", Font.BOLD, 50));
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(text)) / 2;
                int y = 100;
                g.drawString(text, x, y);
            }
            case GAME_OVER -> {
                g.setColor(Color.white);
                String text = "It's not your fault!";
                g.setFont(new Font("Monospaced", Font.BOLD, 28));
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(text)) / 2;
                int y = (int) (height / 2.5);
                g.drawString(text, x, y);

                // restart
                this.add(btnRestart);

                // score
                text = "Your score: " + score;
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = height - 200;
                g.drawString(text, x, y);

                // best score
                if (score > bestScore) {
                    bestScore = score;
                    ScoreStorage.saveBestScore(score);
                    text = "New best score!";
                } else {
                    text = "Best score: " + bestScore;
                }
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y += 50;
                g.drawString(text, x, y);
            }
            default -> {
            }
        }

    }

    private void move() {
        cat.move();

        for (Pipe p : pipes) {
            p.move();

            // are they passed
            if (!p.isPassed() && cat.getX() > p.getX() + p.getWidth()) {
                p.setPassed(true);
                score++;
            }

            // if they are out of frame, we delete them to save memory
            if (p.getX() + p.getWidth() < 0) {
                pipes.remove(p);
            }
        }
    }

    private void gameOver() {
        if (cat.getY() + cat.getSize() >= height || collision()) {
            gameLoop.stop();
            pipeTimer.stop();
            status = GameStatus.GAME_OVER;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            if (status == GameStatus.START) {
                status = GameStatus.PLAY;
                gameLoop.start();
                pipeTimer.start();
            } else if (status == GameStatus.PLAY) {
                cat.boost();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void placePipes() {
        pipes.add(new Pipe(width, height, cat.getSize()));
    }

    private boolean collision() {
        for (Pipe p : pipes) {
            if (cat.getX() + cat.getSize() >= p.getX() && cat.getX() <= p.getX() + p.getWidth() // its between pipes
                    && (cat.getY() <= p.getTopHeight() || cat.getY() + cat.getSize() >= p.getBottomY()) // collision
                    ) {
                return true;
            }
        }
        return false;
    }

    private void restartGame() {
        cat = new Cat(width, height);

        pipes.clear();

        score = 0;

        status = GameStatus.START;

        this.remove(btnRestart);

        repaint();
    }

    private void addRestartButton() {
        btnRestart = new JButton("↻");
        btnRestart.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnRestart.setForeground(Color.white);
        btnRestart.setBackground(Color.orange);
        btnRestart.setBorder(BorderFactory.createLineBorder(Color.white, 2, true));
        btnRestart.setBounds(width - 85, 50, 35, 35);
        btnRestart.addActionListener((ActionEvent e) -> {
            restartGame();
        });
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meow.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import meow.audio.MusicPlayer;
import meow.audio.ScreamPlayer;
import meow.components.Cat;
import meow.components.Pipe;
import meow.enums.GameStatus;
import meow.storage.ScoreStorage;

/**
 *
 * @author niki
 */
public class GamePanel extends JPanel implements KeyListener {

    private final int width;
    private final int height;

    private final Image background;

    private Cat cat;

    private final Timer gameLoop;
    private final Timer pipeTimer;

    private final List<Pipe> pipes;

    GameStatus status;

    private int score;
    private int bestScore;

    private JButton btnRestart;

    private final Image endImg;

    private MusicPlayer musicPlayer;

    private Image jumpscareImg;
    private ScreamPlayer screamPlayer;
    private Timer jumpscareTimer;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(this);

        background = new ImageIcon(getClass().getResource("/resources/images/background.png")).getImage();

        status = GameStatus.START;

        score = 0;

        cat = new Cat(width, height);

        pipes = new ArrayList<>();

        gameLoop = new Timer(1000 / 60, (ActionEvent e) -> {
            move();
            cat.updateAnimation();
            gameOver();
            repaint();
        });

        pipeTimer = new Timer(2500, (ActionEvent e) -> {
            placePipes();
        });

        addRestartButton();
        btnRestart.setVisible(false);

        bestScore = ScoreStorage.loadBestScore();

        endImg = new ImageIcon(getClass().getResource("/resources/images/evil_box.png")).getImage();

        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic();

        jumpscareImg = new ImageIcon(getClass().getResource("/resources/images/cat_jump.png")).getImage();
        screamPlayer = new ScreamPlayer();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        switch (status) {
            case START -> {
                g.setColor(Color.white);
                String text = "MIDNIGHT MEOW: HALLOWEEN EDITION";
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(text)) / 2;
                int y = (int) (height / 2.5);
                g.drawString(text, x, y);

                text = "PRESS SPACE TO START";
                g.setFont(new Font("Monospaced", Font.BOLD, 16));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = (int) (height / 2.5 + 50);
                g.drawString(text, x, y);

                text = "Take a chance and roll the dice";
                g.setFont(new Font("Monospaced", Font.ITALIC, 15));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = height - 100;
                g.drawString(text, x, y);
                text = "Ride with the moon in the dead of night";
                g.setFont(new Font("Monospaced", Font.ITALIC, 15));
                metrics = g.getFontMetrics();
                x = (width - metrics.stringWidth(text)) / 2;
                y = height - 80;
                g.drawString(text, x, y);

            }
            case PLAY -> {
                // background
                if (background != null) {
                    g.drawImage(background, 0, 0, width, height, null);
                    // dark overlay
                    g.setColor(new Color(0, 0, 0, 80));
                    g.fillRect(0, 0, width, height);
                }
                // cat
                if (cat.getImg() != null) {
                    Image sprite = cat.getImg();
                    int frameWidth = sprite.getWidth(null) / cat.getFrameCount();
                    int frameHeight = sprite.getHeight(null);
                    int sourceX = cat.getCurrentFrame() * frameWidth;

                    g.drawImage(sprite,
                            cat.getX(),
                            cat.getY(),
                            cat.getX() + cat.getSize(),
                            cat.getY() + cat.getSize(),
                            sourceX,
                            0,
                            sourceX + frameWidth,
                            frameHeight,
                            null);
                } else {
                    // privremeni prikaz ako slika nije pronadjena
                    g.setColor(Color.orange);
                    g.fillOval(cat.getX(), cat.getY(), cat.getSize(), cat.getSize());
                }

                // pipes
                for (Pipe p : pipes) {
                    if (p.getImg() != null) {
                        // top
                        g.drawImage(p.getImg(), p.getX(), 0, p.getWidth(), p.getTopHeight(), null);
                        // bottom
                        g.drawImage(p.getImg(), p.getX(), p.getBottomY(), p.getWidth(), this.getHeight() - p.getBottomY(), null);
                        // dark overlays
                        g.setColor(new Color(0, 0, 0, 120));
                        g.fillRect(p.getX(), 0, p.getWidth(), p.getTopHeight());
                        g.fillRect(p.getX(), p.getBottomY(), p.getWidth(), this.getHeight() - p.getBottomY());
                    } else {
                        g.setColor(Color.red);
                        // top
                        g.fillRect(p.getX(), 0, p.getWidth(), p.getTopHeight());
                        // bottom
                        g.fillRect(p.getX(), p.getBottomY(), p.getWidth(), this.getHeight() - p.getBottomY());
                    }
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
                String text = "Life's no fun without a good scare";
                g.setFont(new Font("Monospaced", Font.ITALIC, 20));
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(text)) / 2;
                int y = (int) (height / 2.5);
                g.drawString(text, x, y);

                // restart
                btnRestart.setVisible(true);

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

                // end image
                if (endImg != null) {
                    g.drawImage(endImg, width / 2 - 50, 100, 100, 100, null);
                }
            }
            case JUMP -> {
                g.drawImage(jumpscareImg, 0, 0, width, height, null);
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
            musicPlayer.stopMusic();

            startJumpscare();
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
        status = GameStatus.PLAY;

        btnRestart.setVisible(false);

        gameLoop.start();
        pipeTimer.start();

        musicPlayer.playMusic();

        repaint();
        requestFocusInWindow();
    }

    private void addRestartButton() {
        btnRestart = new JButton("↻");
        btnRestart.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnRestart.setForeground(Color.white);
        btnRestart.setBackground(Color.red);
        btnRestart.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        btnRestart.setBounds((width - 160) / 2, height - 320, 160, 45);
        btnRestart.addActionListener((ActionEvent e) -> {
            restartGame();
        });
        btnRestart.setFocusPainted(false);
        btnRestart.setContentAreaFilled(false);
        btnRestart.setOpaque(true);
        btnRestart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        setLayout(null);
        add(btnRestart);
    }

    private void startJumpscare() {
        status = GameStatus.JUMP;

        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
        
        if (screamPlayer != null) {
            screamPlayer.playScream();
        }

        repaint();

        jumpscareTimer = new Timer(2000, e -> {
            jumpscareTimer.stop();
            if (screamPlayer != null) {
                screamPlayer.stopScream();
            }

            status = GameStatus.GAME_OVER;
            repaint();
        });
        jumpscareTimer.setRepeats(false);
        jumpscareTimer.start();
    }

}

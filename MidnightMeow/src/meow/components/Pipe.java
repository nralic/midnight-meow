/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meow.components;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author niki
 */
public class Pipe {

    private int x;
    private final int width;
    private final int topHeight; // how big is top pipe
    private final int bottomY; // where bottom pipe start

    private final int MIN_HEIGHT;
    private final int MAX_HEIGHT;

    private final int gap;

    private Image img;

    private final Random random = new Random();

    private final int velocity;

    private boolean passed;

    public Pipe(int boardWidth, int boardHeight, int catSize) {
        x = boardWidth;

        // sirina cevi prilagodjena macki velicine 64 px
        width = (int) (catSize * 1.2);

        MIN_HEIGHT = boardHeight / 6;
        MAX_HEIGHT = boardHeight - 2 * MIN_HEIGHT;

        topHeight = random.nextInt(MIN_HEIGHT, MAX_HEIGHT + 1);

        gap = (int) (catSize * 2.5);

        bottomY = topHeight + gap;

        velocity = -4;

        img = new ImageIcon(getClass().getResource("/resources/images/pipe.png")).getImage();

    }

    public void move() {
        x += velocity;
    }

    public int getX() {
        return x;
    }

    public int getTopHeight() {
        return topHeight;
    }

    public int getBottomY() {
        return bottomY;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Image getImg() {
        return img;
    }

}

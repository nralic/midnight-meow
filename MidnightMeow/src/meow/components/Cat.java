/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meow.components;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author niki
 */
public class Cat {

    private int x;
    private int y;
    private int size;

    private int velocityY;
    private final int gravity;

    private Image img;

    // animacija
    private final int frameCount = 6;
    private int currentFrame = 0;
    private int aniamationCounter = 0;
    private final int animationSpeed = 5;

    public Cat(int boardWidth, int boardHeight) {
        x = boardWidth / 4;
        y = boardHeight / 2;
        size = 64;

        velocityY = 0;
        gravity = 1;

        // ucitavanje sprite sheet-a
        img = new ImageIcon(getClass().getResource("/resources/images/witch_flying.png")).getImage();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public Image getImg() {
        return img;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void move() {
        velocityY += gravity;
        y += velocityY;

        if (y < 0) {
            y = 0;
            velocityY = 0;
        }
    }

    public void boost() {
        velocityY = -10;
    }

    public void updateAnimation() {
        if (img == null) {
            return;
        }
        aniamationCounter++;

        if (aniamationCounter >= animationSpeed) {
            currentFrame = (currentFrame + 1) % frameCount;
            aniamationCounter = 0;
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smellycat.components;

import java.awt.Image;

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

    private Image startImg;
    private Image flyImg;
    private Image boostImg;
    private Image deadImg;

    public Cat(int boardWidth, int boardHeight) {
        x = boardWidth / 4;
        y = boardHeight / 2;
        size = 32;

        velocityY = 0;
        gravity = 1;
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

}

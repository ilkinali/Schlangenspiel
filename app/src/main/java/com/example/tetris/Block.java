package com.example.tetris;

public class Block {
    private int[][] shape;
    private int color;
    private int x;
    private int y;

    public Block(int[][] shape, int color) {
        this.shape = shape;
        this.color = color;
        this.x = 0;
        this.y = 0;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getColor() {
        return color;
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

    public void rotate() {
        int size = shape.length;
        int[][] newShape = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newShape[i][j] = shape[size - j - 1][i];
            }
        }
        shape = newShape;
    }
}


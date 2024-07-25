package com.example.tetris;

import android.graphics.Color;
import java.util.Random;

public class TetrisGame {
    private int[][] grid;
    private Block currentBlock;
    private Random random;
    private int score;
    private boolean gameOver;

    private static final int[][][] SHAPES = {
            {{1, 1}, {1, 1}}, // Square
            {{1, 1, 1, 1}}, // I
            {{1, 1, 0}, {0, 1, 1}}, // Z
            {{0, 1, 1}, {1, 1, 0}}, // S
            {{1, 1, 1}, {0, 1, 0}}, // T
    };

    private static final int[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN
    };

    public TetrisGame(int width, int height) {
        grid = new int[height][width];
        random = new Random();
        score = 0;
        gameOver = false;
        spawnBlock();
    }

    public void spawnBlock() {
        int shapeIndex = random.nextInt(SHAPES.length);
        int[][] shape = SHAPES[shapeIndex];
        int color = COLORS[random.nextInt(COLORS.length)];
        currentBlock = new Block(shape, color);
        currentBlock.setX(grid[0].length / 2 - shape[0].length / 2);
        currentBlock.setY(0);
        if (!canMove(currentBlock.getX(), currentBlock.getY())) {
            gameOver = true;
        }
    }

    public void moveBlockDown() {
        if (!gameOver) {
            if (canMove(currentBlock.getX(), currentBlock.getY() + 1)) {
                currentBlock.setY(currentBlock.getY() + 1);
            } else {
                placeBlock();
                clearLines();
                spawnBlock();
            }
        }
    }

    public void moveBlockLeft() {
        if (!gameOver && canMove(currentBlock.getX() - 1, currentBlock.getY())) {
            currentBlock.setX(currentBlock.getX() - 1);
        }
    }

    public void moveBlockRight() {
        if (!gameOver && canMove(currentBlock.getX() + 1, currentBlock.getY())) {
            currentBlock.setX(currentBlock.getX() + 1);
        }
    }

    public void rotateBlock() {
        if (!gameOver) {
            currentBlock.rotate();
            if (!canMove(currentBlock.getX(), currentBlock.getY())) {
                for (int i = 0; i < 3; i++) {
                    currentBlock.rotate();
                }
            }
        }
    }

    private boolean canMove(int newX, int newY) {
        int[][] shape = currentBlock.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newGridY = newY + row;
                    int newGridX = newX + col;
                    if (newGridY >= grid.length || newGridX < 0 || newGridX >= grid[0].length || grid[newGridY][newGridX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeBlock() {
        int[][] shape = currentBlock.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    grid[currentBlock.getY() + row][currentBlock.getX() + col] = currentBlock.getColor();
                }
            }
        }
    }

    private void clearLines() {
        int linesCleared = 0;
        for (int row = grid.length - 1; row >= 0; row--) {
            boolean lineFilled = true;
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 0) {
                    lineFilled = false;
                    break;
                }
            }
            if (lineFilled) {
                for (int newRow = row; newRow > 0; newRow--) {
                    grid[newRow] = grid[newRow - 1];
                }
                grid[0] = new int[grid[0].length];
                linesCleared++;
                row++; // Recheck the same row after clearing
            }
        }
        score += linesCleared * 100; // Her temizlenen satır için 100 puan
    }

    public int[][] getGrid() {
        return grid;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}

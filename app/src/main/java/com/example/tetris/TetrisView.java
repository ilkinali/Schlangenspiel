package com.example.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class TetrisView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    private TetrisGame game;
    private int blockSize;
    private Bitmap backgroundBitmap;

    public TetrisView(Context context) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
        screenX = context.getResources().getDisplayMetrics().widthPixels;
        screenY = context.getResources().getDisplayMetrics().heightPixels;
        blockSize = screenX / 10; // 10 columns for the Tetris grid
        game = new TetrisGame(10, 20); // 10 columns and 20 rows
        setOnTouchListener(this);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_image);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenX, screenY, false);
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
            try {
                Thread.sleep(500); // 0.5 saniye bekleyin
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        game.moveBlockDown();
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
            drawGame();
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGame() {
        int[][] grid = game.getGrid();
        Block currentBlock = game.getCurrentBlock();

        // Draw grid
        paint.setColor(Color.GRAY);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    paint.setColor(grid[i][j]);
                    canvas.drawRect(j * blockSize, i * blockSize,
                            (j + 1) * blockSize, (i + 1) * blockSize, paint);
                }
            }
        }

        // Draw current block
        paint.setColor(currentBlock.getColor());
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    canvas.drawRect((currentBlock.getX() + j) * blockSize,
                            (currentBlock.getY() + i) * blockSize,
                            (currentBlock.getX() + j + 1) * blockSize,
                            (currentBlock.getY() + i + 1) * blockSize, paint);
                }
            }
        }

        // Draw score
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("Score: " + game.getScore(), 10, 50, paint);

        // Draw Game Over
        if (game.isGameOver()) {
            paint.setTextSize(100);
            canvas.drawText("Game Over", screenX / 4, screenY / 2, paint);
            playing = false;
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!game.isGameOver()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                if (x < screenX / 2) {
                    game.moveBlockLeft();
                } else {
                    game.moveBlockRight();
                }
                if (y < screenY / 2) {
                    game.rotateBlock();
                } else {
                    game.moveBlockDown();
                }
                return true;
            }
        }
        return false;
    }
}

package com.example.tetris;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TetrisView tetrisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tetrisView = new TetrisView(this);
        setContentView(tetrisView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tetrisView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tetrisView.resume();
    }
}

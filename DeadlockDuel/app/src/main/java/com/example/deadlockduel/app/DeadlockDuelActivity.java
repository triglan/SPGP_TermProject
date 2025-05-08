package com.example.deadlockduel.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.framework.GameView;

public class DeadlockDuelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this);
        setContentView(gameView);  // GameView 표시
    }
}
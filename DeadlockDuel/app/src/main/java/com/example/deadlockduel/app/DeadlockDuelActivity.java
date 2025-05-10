package com.example.deadlockduel.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.GameView;
import com.example.deadlockduel.object.Player;

public class DeadlockDuelActivity extends AppCompatActivity {

    private int turnCount = 0;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        GameView gameView = findViewById(R.id.gameView);
        player = gameView.getPlayer(); // GameView에서 player 불러오기

        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnRotate = findViewById(R.id.btnRotate);

        btnLeft.setOnClickListener(v -> {
            player.moveLeft();
            nextTurn();
        });

        btnRight.setOnClickListener(v -> {
            player.moveRight();
            nextTurn();
        });

        btnRotate.setOnClickListener(v -> {
            player.rotate();
            nextTurn();
        });
    }

    private void nextTurn() {
        turnCount++;
        Log.d("TurnSystem", "현재 턴: " + turnCount);
    }
}

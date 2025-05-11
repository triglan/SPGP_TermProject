package com.example.deadlockduel.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.GameView;
import com.example.deadlockduel.framework.AttackType;
import com.example.deadlockduel.object.Player;

public class DeadlockDuelActivity extends AppCompatActivity {

    private int turnCount = 0;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        GameView gameView = findViewById(R.id.gameView);
        player = gameView.getPlayer();

        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnRotate = findViewById(R.id.btnRotate);

        ImageButton btnAttack1 = findViewById(R.id.btnAttack1);
        ImageButton btnAttack2 = findViewById(R.id.btnAttack2);
        ImageButton btnAttack3 = findViewById(R.id.btnAttack3);

        btnLeft.setOnClickListener(v -> {
            player.moveLeft();
            gameView.onPlayerActionFinished();
        });

        btnRight.setOnClickListener(v -> {
            player.moveRight();
            gameView.onPlayerActionFinished();
        });

        btnRotate.setOnClickListener(v -> {
            player.rotate();
            gameView.onPlayerActionFinished();
        });

        // 공격 버튼 눌렀을 때 이펙트 테스트 출력
        btnAttack1.setOnClickListener(v -> gameView.playEffect(AttackType.BASIC));
        btnAttack2.setOnClickListener(v -> gameView.playEffect(AttackType.LONG_RANGE));
        btnAttack3.setOnClickListener(v -> gameView.playEffect(AttackType.POWER));
    }

    private void nextTurn() {
        turnCount++;
        Log.d("TurnSystem", "현재 턴: " + turnCount);
    }
}

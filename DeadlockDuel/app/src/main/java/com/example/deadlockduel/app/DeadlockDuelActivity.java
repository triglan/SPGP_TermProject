package com.example.deadlockduel.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.core.GameView;
import com.example.deadlockduel.framework.manager.StageManager;
import com.example.deadlockduel.object.Player;
import com.example.deadlockduel.scene.MainScene;

public class DeadlockDuelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        GameView gameView = findViewById(R.id.gameView);

        // ✅ StageManager 생성
        StageManager stageManager = new StageManager();

        // ✅ MainScene 생성 시 StageManager 넘김
        MainScene mainScene = new MainScene(
                getResources(),
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                stageManager
        );
        gameView.setInitialScene(mainScene);

        Player player = mainScene.getPlayer();

        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnRotate = findViewById(R.id.btnRotate);
        ImageButton btnAttack1 = findViewById(R.id.btnAttack1);
        ImageButton btnAttack2 = findViewById(R.id.btnAttack2);
        ImageButton btnAttack3 = findViewById(R.id.btnAttack3);

        btnLeft.setOnClickListener(v -> player.moveLeft());
        btnRight.setOnClickListener(v -> player.moveRight());
        btnRotate.setOnClickListener(v -> player.rotate());

        btnAttack1.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.BASIC));
        btnAttack2.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.LONG_RANGE));
        btnAttack3.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.POWER));

        Log.d("DeadlockDuelActivity", "MainScene initialized");
    }
}

package com.example.deadlockduel.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.AttackType;
import com.example.deadlockduel.framework.EnemySpawnData;
import com.example.deadlockduel.framework.GameView;
import com.example.deadlockduel.framework.StageConfig;
import com.example.deadlockduel.object.Player;
import com.example.deadlockduel.scene.MainScene;

import java.util.Arrays;

public class DeadlockDuelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        GameView gameView = findViewById(R.id.gameView);

        // ✅ 스테이지 설정
        StageConfig config = new StageConfig(
                R.drawable.map1,
                7,
                3,
                true,
                Arrays.asList(
                        new EnemySpawnData("knight", 5, false),
                        new EnemySpawnData("archer", 6, true)
                )
        );

        // ✅ MainScene 생성 및 설정
        MainScene mainScene = new MainScene(
                getResources(),
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                config
        );
        gameView.setInitialScene(mainScene);

        // ✅ Player 가져오기
        Player player = mainScene.getPlayer();

        // ✅ 버튼 바인딩 및 이벤트
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnRotate = findViewById(R.id.btnRotate);
        ImageButton btnAttack1 = findViewById(R.id.btnAttack1);
        ImageButton btnAttack2 = findViewById(R.id.btnAttack2);
        ImageButton btnAttack3 = findViewById(R.id.btnAttack3);

        btnLeft.setOnClickListener(v -> {
            player.moveLeft();
        });

        btnRight.setOnClickListener(v -> {
            player.moveRight();
        });

        btnRotate.setOnClickListener(v -> {
            player.rotate();
        });

        btnAttack1.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.BASIC));
        btnAttack2.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.LONG_RANGE));
        btnAttack3.setOnClickListener(v -> mainScene.handlePlayerTurn(AttackType.POWER));

        Log.d("DeadlockDuelActivity", "MainScene initialized");
    }
}

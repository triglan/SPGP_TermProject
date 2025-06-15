package com.example.deadlockduel.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.core.GameView;
import com.example.deadlockduel.framework.manager.StageManager;
import com.example.deadlockduel.scene.MainScene;

public class DeadlockDuelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        GameView gameView = findViewById(R.id.gameView);

        // StageManager 및 MainScene 생성
        StageManager stageManager = new StageManager();
        MainScene mainScene = new MainScene(
                getResources(),
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                stageManager
        );
        gameView.setInitialScene(mainScene);

        // 버튼 연결
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnRotate = findViewById(R.id.btnRotate);
        ImageButton btnAttack1 = findViewById(R.id.btnAttack1);
        ImageButton btnAttack2 = findViewById(R.id.btnAttack2);
        ImageButton btnAttack3 = findViewById(R.id.btnAttack3);
        Button btnExecute = findViewById(R.id.btnAttackExecute);  // XML에서 주석 해제 필수

        // 이동, 회전 처리
        btnLeft.setOnClickListener(v -> mainScene.handlePlayerMoveLeft());
        btnRight.setOnClickListener(v -> mainScene.handlePlayerMoveRight());
        btnRotate.setOnClickListener(v -> mainScene.handlePlayerRotate());

        // 공격 대기열에 추가 (무기 0, 1, 2번)
        btnAttack1.setOnClickListener(v -> mainScene.enqueueAttackFromButton(0));
        btnAttack2.setOnClickListener(v -> mainScene.enqueueAttackFromButton(1));
        btnAttack3.setOnClickListener(v -> mainScene.enqueueAttackFromButton(2));

        // 공격 큐 실행
        btnExecute.setOnClickListener(v -> mainScene.executeAttackFromButton());
    }
}

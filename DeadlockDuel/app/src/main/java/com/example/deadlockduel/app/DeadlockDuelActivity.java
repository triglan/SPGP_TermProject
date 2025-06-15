package com.example.deadlockduel.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.core.GameView;
import com.example.deadlockduel.framework.manager.StageManager;
import com.example.deadlockduel.object.Player;
import com.example.deadlockduel.scene.MainScene;
import com.example.deadlockduel.scene.SceneManager;
import com.example.deadlockduel.scene.Scene;


public class DeadlockDuelActivity extends AppCompatActivity {
    private GameView gameView;
    private MainScene mainScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlock_duel);

        gameView = findViewById(R.id.gameView);
        StageManager stageManager = new StageManager();

        mainScene = new MainScene(
                getResources(),
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                stageManager,
                this
        );
        gameView.setInitialScene(mainScene);

        // 버튼 세팅 생략...
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mainScene != null) mainScene.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainScene != null) mainScene.onResume();
    }
}


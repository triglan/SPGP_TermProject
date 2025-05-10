package com.example.deadlockduel.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.R;

public class MainActivity extends AppCompatActivity {

    // ✅ 개발 중이면 true
    private static final boolean DEV_MODE_SKIP_MAIN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEV_MODE_SKIP_MAIN) {//메인화면 스킵
            Intent intent = new Intent(this, DeadlockDuelActivity.class);
            startActivity(intent);
            finish(); // MainActivity 종료
            return;
        }

        // 일반 모드일 경우: 메인 화면 보여주기
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeadlockDuelActivity.class);
                startActivity(intent);
            }
        });
    }
}

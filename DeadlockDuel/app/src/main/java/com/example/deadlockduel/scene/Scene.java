// framework.scene.Scene.java
package com.example.deadlockduel.scene;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    void update();
    void draw(Canvas canvas);
    boolean onTouchEvent(MotionEvent event);
    void onEnter();     // 씬 들어올 때
    void onExit();      // 씬 빠질 때
    void onPause();     // 앱 일시정지 시
    void onResume();    // 앱 다시 복귀 시
    boolean onBackPressed(); // 뒤로가기 처리
}

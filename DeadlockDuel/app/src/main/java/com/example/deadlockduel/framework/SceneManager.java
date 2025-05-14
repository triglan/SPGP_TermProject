package com.example.deadlockduel.framework;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class SceneManager {
    private Scene currentScene;

    public void setScene(Scene scene) {
        if (currentScene != null) currentScene.onExit();
        currentScene = scene;
        currentScene.onEnter();
    }

    public void update() {
        if (currentScene != null) currentScene.update();
    }

    public void draw(Canvas canvas) {
        if (currentScene != null) currentScene.draw(canvas);
    }

    public boolean handleTouch(MotionEvent event) {
        return currentScene != null && currentScene.onTouchEvent(event);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}

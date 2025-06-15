package com.example.deadlockduel.framework.core;

import android.view.MotionEvent;

import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.scene.MainScene;

public class TouchInputHandler {
    private final MainScene scene;

    public TouchInputHandler(MainScene scene) {
        this.scene = scene;
    }

    public boolean handleTouch(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

        float x = event.getX();
        float screenWidth = scene.getBlockRect(0).right * 13f;


        if (x < screenWidth / 3f) {
            scene.getPlayer().moveLeft();   // 왼쪽으로 이동
        } else if (x < screenWidth * 2f / 3f) {
            scene.getPlayer().moveRight();  // 오른쪽으로 이동
        } else {
            scene.handlePlayerAttack(AttackType.MELEE); // 공격
        }


        return true;
    }

}

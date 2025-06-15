package com.example.deadlockduel.framework.core;

import com.example.deadlockduel.scene.MainScene;
import com.example.deadlockduel.framework.battle.AttackType;

public class TurnProcessor {
    private final MainScene scene;

    public TurnProcessor(MainScene scene) {
        this.scene = scene;
    }


    public void advanceTurn() {
        scene.incrementTurnCount();   // turnCount++
        scene.updateEnemies();        // 적 행동
        scene.getPlayer().onTurnPassed();        // 쿨타임 회복 추가
        scene.updateCooldownUI();    // UI도 갱신
    }

}

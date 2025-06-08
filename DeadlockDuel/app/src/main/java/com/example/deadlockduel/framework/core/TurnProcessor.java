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
        scene.executeAllAttacks();    // 적 공격 or 기타 효과
    }

}

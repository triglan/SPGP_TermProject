package com.example.deadlockduel.framework.core;

import com.example.deadlockduel.scene.MainScene;
import com.example.deadlockduel.framework.battle.AttackType;

public class TurnProcessor {
    private final MainScene scene;

    public TurnProcessor(MainScene scene) {
        this.scene = scene;
    }


    public void advanceTurn() {
        scene.incrementTurnCount();
        scene.updateEnemies();
        scene.getPlayer().advanceCooldowns();
    }

}

package com.example.deadlockduel.framework;

import com.example.deadlockduel.R;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private final List<StageConfig> stages = new ArrayList<>();
    private int currentStageIndex = 0;

    public StageManager() {
        // 🗺️ Stage 1: Knight 1마리
        List<EnemySpawnData> enemies1 = new ArrayList<>();
        enemies1.add(new EnemySpawnData("knight", 4, true));
        stages.add(new StageConfig(R.drawable.map1, 5, 0, true, enemies1));

//        // 🗺️ Stage 2: Knight + Archer
//        List<EnemySpawnData> enemies2 = new ArrayList<>();
//        enemies2.add(new EnemySpawnData("knight", 3, true));
//        enemies2.add(new EnemySpawnData("archer", 4, true));
//        stages.add(new StageConfig(R.drawable.map2, 6, 1, false, enemies2));
//
//        // 🗺️ Stage 3: Knight + Archer + Rogue
//        List<EnemySpawnData> enemies3 = new ArrayList<>();
//        enemies3.add(new EnemySpawnData("knight", 2, true));
//        enemies3.add(new EnemySpawnData("archer", 4, true));
//        enemies3.add(new EnemySpawnData("rogue", 5, false));
//        stages.add(new StageConfig(R.drawable.map3, 7, 3, true, enemies3));
    }

    public StageConfig getCurrentStage() {
        return stages.get(currentStageIndex);
    }

    public boolean hasNext() {
        return currentStageIndex + 1 < stages.size();
    }

    public void nextStage() {
        if (hasNext()) {
            currentStageIndex++;
        }
    }

    public int getCurrentStageNumber() {
        return currentStageIndex + 1;
    }

    public void reset() {
        currentStageIndex = 0;
    }
}

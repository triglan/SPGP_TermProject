package com.example.deadlockduel.framework.manager;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.data.EnemySpawnData;
import com.example.deadlockduel.framework.data.StageConfig;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private final List<StageConfig> stages = new ArrayList<>();
    private int currentStageIndex = 0;

    public StageManager() {
        // 🗺️ Stage 1: Knight 1마리
        List<EnemySpawnData> enemies1 = new ArrayList<>();
        enemies1.add(new EnemySpawnData("knight", 6, false));
        enemies1.add(new EnemySpawnData("knight", 5, false));
        enemies1.add(new EnemySpawnData("rogue", 4, false));
        addStage(R.drawable.map1, 7, 0, true, enemies1);

        // 🗺️ Stage 2: Knight + Archer
        List<EnemySpawnData> enemies2 = new ArrayList<>();
        enemies2.add(new EnemySpawnData("knight", 7, false));
        enemies2.add(new EnemySpawnData("knight", 6, false));
        enemies2.add(new EnemySpawnData("archer", 8, false));
        enemies2.add(new EnemySpawnData("rogue", 0, true));
        addStage(R.drawable.map2, 9, 1, false, enemies2);

        // 🗺️ Stage 3: Knight + Archer + Rogue
        List<EnemySpawnData> enemies3 = new ArrayList<>();
        enemies3.add(new EnemySpawnData("knight", 8, true));
        enemies3.add(new EnemySpawnData("archer", 9, true));
        enemies3.add(new EnemySpawnData("rogue", 10, false));
        enemies3.add(new EnemySpawnData("rogue", 1, false));
        enemies3.add(new EnemySpawnData("knight", 0, false));
        addStage(R.drawable.map3, 11, 4, true, enemies3);
    }

    //  스테이지 추가 헬퍼 메서드
    private void addStage(int backgroundResId, int blockCount, int playerStartIndex, boolean playerFaceRight, List<EnemySpawnData> enemies) {
        stages.add(new StageConfig(backgroundResId, blockCount, playerStartIndex, playerFaceRight, enemies));
    }

    public StageConfig getCurrentStage() {
        return stages.get(currentStageIndex);
    }

    public boolean hasNext() {
        return currentStageIndex + 1 < stages.size();
    }
    public StageConfig resetAndGetFirstStage() {
        currentStageIndex = 0;
        return stages.get(currentStageIndex);
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

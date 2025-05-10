package com.example.deadlockduel.framework;

import com.example.deadlockduel.R;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private final List<StageConfig> stages = new ArrayList<>();
    private int currentStageIndex = 0;

    public StageManager() {
        // ✅ 스테이지 정보 등록
        stages.add(new StageConfig(R.drawable.map1, 5, 0, true));
        //stages.add(new StageConfig(R.drawable.map2, 6, 1, false));
        //stages.add(new StageConfig(R.drawable.map3, 7, 3, true));
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

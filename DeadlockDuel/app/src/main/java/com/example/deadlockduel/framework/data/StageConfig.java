package com.example.deadlockduel.framework.data;

import java.util.List;

public class StageConfig {
    public final int backgroundResId;
    public int blockCount;
    public final int playerStartIndex;
    public final boolean playerFaceRight;
    public final List<EnemySpawnData> enemies;

    public StageConfig(int backgroundResId, int blockCount, int playerStartIndex,
                       boolean playerFaceRight, List<EnemySpawnData> enemies) {
        this.backgroundResId = backgroundResId;
        this.blockCount = blockCount;
        this.playerStartIndex = playerStartIndex;
        this.playerFaceRight = playerFaceRight;
        this.enemies = enemies;
    }

    public int getBlockCount() {
        return blockCount;
    }
}

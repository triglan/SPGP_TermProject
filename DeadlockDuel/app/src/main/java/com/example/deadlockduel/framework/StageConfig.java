// framework/StageConfig.java

package com.example.deadlockduel.framework;

public class StageConfig {
    public final int backgroundResId;
    public final int blockCount;
    public final int playerStartIndex;
    public final boolean playerFaceRight;

    public StageConfig(int backgroundResId, int blockCount, int playerStartIndex, boolean playerFaceRight) {
        this.backgroundResId = backgroundResId;
        this.blockCount = blockCount;
        this.playerStartIndex = playerStartIndex;
        this.playerFaceRight = playerFaceRight;
    }
}

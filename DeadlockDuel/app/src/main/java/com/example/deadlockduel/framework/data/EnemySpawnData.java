package com.example.deadlockduel.framework.data;

public class EnemySpawnData {
    public final String type;
    public final int blockIndex;
    public final boolean faceRight;

    public EnemySpawnData(String type, int blockIndex, boolean faceRight) {
        this.type = type;
        this.blockIndex = blockIndex;
        this.faceRight = faceRight;
    }
}
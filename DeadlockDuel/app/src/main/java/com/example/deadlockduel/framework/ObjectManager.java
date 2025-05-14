package com.example.deadlockduel.framework;

import android.content.res.Resources;
import android.graphics.Rect;

import com.example.deadlockduel.framework.EnemySpawnData;
import com.example.deadlockduel.framework.StageConfig;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.Enemy_Knight;
import com.example.deadlockduel.object.Enemy_Archer;
import com.example.deadlockduel.object.Enemy_Rogue;
import com.example.deadlockduel.object.Player;

import java.util.ArrayList;
import java.util.List;

public class ObjectManager {
    private final Block[] blocks;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Player player;
    private final int blockCount;

    public ObjectManager(Resources res, int screenWidth, int screenHeight, StageConfig config) {
        this.blockCount = config.blockCount;
        this.blocks = initBlocks(screenWidth, screenHeight, blockCount);

        player = new Player(res);
        player.setBlockCount(blockCount);
        player.reset(config.playerStartIndex, config.playerFaceRight);

        for (EnemySpawnData spawn : config.enemies) {
            Enemy enemy = createEnemyFromType(res, spawn.type, spawn.blockIndex, spawn.faceRight);
            enemy.setBlockCount(blockCount);
            enemy.updateBlockState(blocks);
            enemy.updatePositionFromBlock(blocks[spawn.blockIndex].getRect());
            enemies.add(enemy);
        }
    }

    private Block[] initBlocks(int screenWidth, int screenHeight, int count) {
        int gridCount = 13;
        Block[] blocks = new Block[count];

        int gridWidth = screenWidth / gridCount;
        int blockWidth = gridWidth;
        int blockHeight = blockWidth / 5;
        int top = (int)(screenHeight * 4f / 5f - blockHeight / 2);
        int startIndex = (gridCount - count) / 2;

        for (int i = 0; i < count; i++) {
            int gridIndex = startIndex + i;
            int left = gridIndex * gridWidth;
            Rect rect = new Rect(left, top, left + blockWidth, top + blockHeight);
            blocks[i] = new Block(rect);
        }

        return blocks;
    }

    private Enemy createEnemyFromType(Resources res, String type, int index, boolean faceRight) {
        switch (type) {
            case "knight": return new Enemy_Knight(res, index, faceRight);
            case "archer": return new Enemy_Archer(res, index, faceRight);
            case "rogue":  return new Enemy_Rogue(res, index, faceRight);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public Rect getBlockRect(int index) {
        return blocks[index].getRect();
    }
}

package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Rect;

import com.example.deadlockduel.framework.data.StageConfig;
import com.example.deadlockduel.framework.data.EnemySpawnData;

import java.util.ArrayList;
import java.util.List;

public class ObjectManager {

    private final List<Enemy> enemies = new ArrayList<>();
    private final Block[] blocks;
    private final Player player;
    private final int blockCount;

    public ObjectManager(Resources res, int screenWidth, int screenHeight, StageConfig config) {
        this.blockCount = config.blockCount;

        blocks = new Block[blockCount];
        int blockWidth = screenWidth / blockCount;
        int blockHeight = blockWidth / 2;
        for (int i = 0; i < blockCount; i++) {
            int left = i * blockWidth;
            int top = screenHeight - blockHeight;
            blocks[i] = new Block(left, top, blockWidth, blockHeight);
        }

        player = new Player(res);
        player.setBlockCount(blockCount);
        player.setBlockIndex(config.playerStartIndex);
        player.reset(config.playerStartIndex, config.playerFaceRight);

        for (EnemySpawnData enemyConfig : config.enemies) {
            Enemy enemy = new Enemy(res, enemyConfig.type);
            enemy.setBlockIndex(enemyConfig.blockIndex);
            enemy.setDirection(enemyConfig.faceRight ? 1 : -1);
            enemy.setBlockCount(blockCount);
            enemies.add(enemy);
        }

        updateAllBlockStates();
    }

    public void updateAllBlockStates() {
        for (Block block : blocks) {
            block.setHasPlayer(false);
            block.setHasEnemy(false);
        }
        player.updateBlockState(blocks);
        for (Enemy enemy : enemies) {
            enemy.updateBlockState(blocks);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemyList() {
        return enemies;
    }

    // ✅ MainScene 호환용 배열 반환
    public Enemy[] getEnemies() {
        return enemies.toArray(new Enemy[0]);
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public Rect getBlockRect(int index) {
        if (index < 0 || index >= blocks.length) return null;
        return blocks[index].getRect();
    }

    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.updateAnimation(); // 애니메이션 프레임 갱신
        }
    }
}

package com.example.deadlockduel.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.EnemySpawnData;
import com.example.deadlockduel.framework.Scene;
import com.example.deadlockduel.framework.StageConfig;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.Enemy_Knight;
import com.example.deadlockduel.object.Player;

import java.util.ArrayList;
import java.util.List;

public class MainScene implements Scene {
    private Bitmap background;
    private Block[] blocks;
    private Player player;
    private Paint blockPaint = new Paint();
    private List<Enemy> enemies;

    private int blockCount;

    public Player getPlayer() {
        return this.player;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public MainScene(Resources res, int screenWidth, int screenHeight, StageConfig config) {
        background = BitmapFactory.decodeResource(res, config.backgroundResId);

        this.blockCount = config.blockCount;
        int gridCount = 13;
        blocks = new Block[blockCount];

        int gridWidth = screenWidth / gridCount;
        int blockWidth = gridWidth;
        int blockHeight = blockWidth / 5;
        int top = (int)(screenHeight * 4f / 5f - blockHeight / 2);
        int startIndex = (gridCount - blockCount) / 2;

        for (int i = 0; i < blockCount; i++) {
            int gridIndex = startIndex + i;
            int left = gridIndex * gridWidth;
            Rect rect = new Rect(left, top, left + blockWidth, top + blockHeight);
            blocks[i] = new Block(rect);
        }

        player = new Player(res);
        player.setBlockCount(blockCount);
        player.reset(config.playerStartIndex, config.playerFaceRight);

        enemies = new ArrayList<>();
        for (EnemySpawnData spawn : config.enemies) {
            Enemy enemy = createEnemyFromType(res, spawn.type, spawn.blockIndex, spawn.faceRight);
            enemy.setBlockCount(blockCount);
            enemy.updateBlockState(blocks);
            enemy.updatePositionFromBlock(blocks[spawn.blockIndex].getRect());
            enemies.add(enemy);
        }
    }

    private Enemy createEnemyFromType(Resources res, String type, int index, boolean faceRight) {
        switch (type) {
            case "knight":
                return new Enemy_Knight(res, index, faceRight);
            // case "archer": return new Enemy_Archer(...);
            // case "rogue": return new Enemy_Rogue(...);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    // 플레이어만 업데이트 (턴 중 항상 호출됨)
    @Override
    public void update() {
        for (Block block : blocks) {
            block.reset();
        }

        player.update();
        player.updateBlockState(blocks);
        for (Enemy enemy : enemies) {
            enemy.update(); // ✅ 매 프레임마다 호출
            enemy.updateBlockState(blocks);
        }
    }

    // 적의 턴일 때 호출 (GameView에서 직접 호출)
    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update(player);
            enemy.updateBlockState(blocks);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(background, null, dst, null);

        for (Block block : blocks) {
            block.draw(canvas, blockPaint);
        }

        for (Enemy enemy : enemies) {
            Rect blockRect = blocks[enemy.getBlockIndex()].getRect();
            enemy.updatePositionFromBlock(blockRect);
            enemy.draw(canvas);
        }

        Rect playerBlockRect = blocks[player.getBlockIndex()].getRect();
        player.updatePositionFromBlock(playerBlockRect);
        player.draw(canvas);
    }

    @Override public boolean onTouchEvent(MotionEvent event) { return false; }
    @Override public void onEnter() { }
    @Override public void onExit() { }
    @Override public void onPause() { }
    @Override public void onResume() { }
    @Override public boolean onBackPressed() { return false; }
}

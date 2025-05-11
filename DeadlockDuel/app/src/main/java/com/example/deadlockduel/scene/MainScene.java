package com.example.deadlockduel.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.deadlockduel.framework.EnemySpawnData;
import com.example.deadlockduel.framework.Scene;
import com.example.deadlockduel.framework.StageConfig;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.Enemy_Knight;
import com.example.deadlockduel.object.Enemy_Archer;
import com.example.deadlockduel.object.Enemy_Rogue;
import com.example.deadlockduel.object.Player;
import com.example.deadlockduel.framework.AttackCommand;
import com.example.deadlockduel.framework.AttackEffect;
import com.example.deadlockduel.framework.AttackType;
import com.example.deadlockduel.R;

import java.util.ArrayList;
import java.util.List;

public class MainScene implements Scene {
    private Bitmap background;
    private Block[] blocks;
    private Player player;
    private Paint blockPaint = new Paint();
    private List<Enemy> enemies;
    private final List<AttackCommand> attackQueue = new ArrayList<>();
    private final List<AttackEffect> effects = new ArrayList<>();
    private final Paint effectPaint = new Paint();
    private int blockCount;

    public Player getPlayer() {
        return this.player;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public List<AttackEffect> getEffects() {
        return effects;
    }

    public Rect getBlockRect(int index) {
        return blocks[index].getRect();
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

        // 무기별 이펙트 설정
        AttackType.BASIC.effectFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_1),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_2),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_3),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_4)
        };
        AttackType.BASIC.effectFacesRight = true;
        AttackType.BASIC.offsetY = -40;

        AttackType.LONG_RANGE.effectFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_range_1),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_range_2)
        };
        AttackType.LONG_RANGE.effectFacesRight = true;
        AttackType.LONG_RANGE.offsetY = -10;

        AttackType.POWER.effectFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_power_1),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_power_2),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_power_3)
        };
        AttackType.POWER.effectFacesRight = true;
        AttackType.POWER.offsetY = -30;
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

    @Override
    public void update() {
        for (Block block : blocks) {
            block.reset();
        }

        player.update();
        player.updateBlockState(blocks);
        for (Enemy enemy : enemies) {
            enemy.updateAnimation();
            enemy.updateBlockState(blocks);
        }

        for (int i = effects.size() - 1; i >= 0; i--) {
            AttackEffect effect = effects.get(i);
            effect.update();
            if (effect.isDone()) {
                effects.remove(i);
            }
        }

    }

    public void updateEnemies() {
        Enemy[] enemyArray = enemies.toArray(new Enemy[0]);
        for (Enemy enemy : enemies) {
            enemy.act(player, enemyArray, attackQueue);
            enemy.updateBlockState(blocks);
        }
    }

    public void performAttack(AttackType type) {
        AttackCommand cmd = new AttackCommand(type, player, false); // perfectTiming은 일단 false로 처리
        cmd.execute(
                enemies.toArray(new Enemy[0]),
                effects,
                player,
                blocks
        );
    }

    public void executeNextAttack() {
        if (attackQueue.isEmpty()) return;
        AttackCommand cmd = attackQueue.remove(0);
        cmd.execute(
                enemies.toArray(new Enemy[0]),
                effects,
                player,
                blocks
        );
    }

    public void executeAllAttacks() {
        while (!attackQueue.isEmpty()) {
            executeNextAttack();
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

        for (AttackEffect effect : effects) {
            effect.draw(canvas, effectPaint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) { return false; }
    @Override public void onEnter() { }
    @Override public void onExit() { }
    @Override public void onPause() { }
    @Override public void onResume() { }
    @Override public boolean onBackPressed() { return false; }
}

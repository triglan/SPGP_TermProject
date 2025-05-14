package com.example.deadlockduel.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackEffect;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.battle.EffectManager;
import com.example.deadlockduel.framework.core.TouchInputHandler;
import com.example.deadlockduel.framework.core.TurnProcessor;
import com.example.deadlockduel.framework.data.StageConfig;
import com.example.deadlockduel.framework.manager.StageManager;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.ObjectManager;
import com.example.deadlockduel.object.Player;

import java.util.ArrayList;
import java.util.List;

public class MainScene implements Scene {
    private Bitmap background;
    private Paint blockPaint = new Paint();
    private ObjectManager objectManager;
    private TurnProcessor turnProcessor;
    private EffectManager effectManager;
    private TouchInputHandler inputHandler;
    private final List<AttackCommand> attackQueue = new ArrayList<>();

    private final Resources res;
    private final int screenWidth, screenHeight;
    private final StageManager stageManager;
    private StageConfig config;

    public MainScene(Resources res, int screenWidth, int screenHeight, StageManager stageManager) {
        this.res = res;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.stageManager = stageManager;
        this.config = stageManager.getCurrentStage();

        initStage();
        initEffects();
    }

    private void initStage() {
        background = BitmapFactory.decodeResource(res, config.backgroundResId);
        objectManager = new ObjectManager(res, screenWidth, screenHeight, config);
        turnProcessor = new TurnProcessor(this);
        effectManager = new EffectManager();
        inputHandler = new TouchInputHandler(this);
    }

    private void initEffects() {
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

    public void goToNextStageIfAvailable() {
        if (stageManager.hasNext()) {
            stageManager.nextStage();
            config = stageManager.getCurrentStage();
            initStage();
            initEffects(); // 이펙트도 초기화 필요할 경우
        }
    }

    public void update() {
        for (Block block : objectManager.getBlocks()) block.reset();

        objectManager.getPlayer().update();
        objectManager.getPlayer().updateBlockState(objectManager.getBlocks());

        for (Enemy enemy : objectManager.getEnemies()) {
            enemy.updateAnimation();
            enemy.updateBlockState(objectManager.getBlocks());
        }

        effectManager.update();
    }

    public void updateEnemies() {
        Enemy[] enemyArray = objectManager.getEnemies().toArray(new Enemy[0]);
        for (Enemy enemy : objectManager.getEnemies()) {
            enemy.act(objectManager.getPlayer(), enemyArray, attackQueue);
            enemy.updateBlockState(objectManager.getBlocks());
        }
    }

    public void performAttack(AttackType type) {
        AttackCommand cmd = new AttackCommand(type, objectManager.getPlayer(), false);
        cmd.execute(
                objectManager.getEnemies().toArray(new Enemy[0]),
                effectManager.getEffects(),
                objectManager.getPlayer(),
                objectManager.getBlocks()
        );
    }

    public void executeNextAttack() {
        if (attackQueue.isEmpty()) return;
        AttackCommand cmd = attackQueue.remove(0);
        cmd.execute(
                objectManager.getEnemies().toArray(new Enemy[0]),
                effectManager.getEffects(),
                objectManager.getPlayer(),
                objectManager.getBlocks()
        );
    }

    public void executeAllAttacks() {
        while (!attackQueue.isEmpty()) {
            executeNextAttack();
        }
    }

    public void handlePlayerTurn(AttackType type) {
        turnProcessor.handlePlayerTurn(type);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(background, null, dst, null);

        for (Block block : objectManager.getBlocks()) block.draw(canvas, blockPaint);

        for (Enemy enemy : objectManager.getEnemies()) {
            Rect blockRect = objectManager.getBlockRect(enemy.getBlockIndex());
            enemy.updatePositionFromBlock(blockRect);
            enemy.draw(canvas);
        }

        Player player = objectManager.getPlayer();
        Rect playerBlockRect = objectManager.getBlockRect(player.getBlockIndex());
        player.updatePositionFromBlock(playerBlockRect);
        player.draw(canvas);

        effectManager.draw(canvas);
    }

    public Player getPlayer() { return objectManager.getPlayer(); }
    public int getBlockCount() { return objectManager.getBlockCount(); }
    public List<AttackEffect> getEffects() { return effectManager.getEffects(); }
    public Rect getBlockRect(int index) { return objectManager.getBlockRect(index); }

    @Override public boolean onTouchEvent(MotionEvent event) { return inputHandler.handleTouch(event); }
    @Override public void onEnter() { }
    @Override public void onExit() { }
    @Override public void onPause() { }
    @Override public void onResume() { }
    @Override public boolean onBackPressed() { return false; }
}

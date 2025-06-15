package com.example.deadlockduel.scene;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackEffect;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.battle.EffectManager;
import com.example.deadlockduel.framework.core.BlockRectProvider;
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

public class MainScene implements Scene, BlockRectProvider {
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
    private int turnCount = 1;
    // 턴 애니메이션
    private long turnAnimStartTime = -1;
    private final long TURN_ANIM_DURATION = 1000; // milliseconds
    private boolean turnAnimActive = false;
    private final Context context;

    public MainScene(Resources res, int screenWidth, int screenHeight, StageManager stageManager, Context context) {
        this.res = res;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.stageManager = stageManager;
        this.config = stageManager.getCurrentStage();
        this.context = context;

        initStage();
        initEffects();
    }

    private void initStage() {
        background = BitmapFactory.decodeResource(res, config.backgroundResId);
        objectManager = new ObjectManager(res, screenWidth, screenHeight, config);
        turnProcessor = new TurnProcessor(this);
        effectManager = EffectManager.getInstance();
        inputHandler = new TouchInputHandler(this);
        objectManager.getPlayer().setBlockRectProvider(this);
    }

    private void initEffects() {
        AttackType.MELEE.effectFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_1),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_2),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_3),
                BitmapFactory.decodeResource(res, R.drawable.attack_effect_melee_4)
        };
        AttackType.MELEE.effectFacesRight = true;
        AttackType.MELEE.offsetY = -40;

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


    public void update() {
        for (Block block : objectManager.getBlocks()) block.reset();

        objectManager.getPlayer().update(objectManager.getEnemies());
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

    public void executeEnemyAttacks() {
        List<AttackEffect> effects = new ArrayList<>();

        for (AttackCommand cmd : attackQueue) {
            cmd.execute(
                    objectManager.getEnemies().toArray(new Enemy[0]),
                    effects,
                    objectManager.getPlayer(),
                    objectManager.getBlocks()
            );
        }

        for (AttackEffect effect : effects) {
            EffectManager.getInstance().addEffect(effect);
        }

        attackQueue.clear();
    }


    public void handlePlayerExecuteAttack() {
        objectManager.getPlayer().startAttackQueue();
        updateCooldownUI();
    }
    public void updateCooldownUI() {
        int[] cd = objectManager.getPlayer().getWeaponCooldowns();
        int[] max = objectManager.getPlayer().getMaxCooldowns();

        ((TextView) ((Activity) context).findViewById(R.id.textCooldown1)).setText(cd[0] + "/" + max[0]);
        ((TextView) ((Activity) context).findViewById(R.id.textCooldown2)).setText(cd[1] + "/" + max[1]);
        ((TextView) ((Activity) context).findViewById(R.id.textCooldown3)).setText(cd[2] + "/" + max[2]);
    }

    public void handlePlayerMoveLeft() {
        objectManager.getPlayer().moveLeft();
        turnProcessor.advanceTurn();
    }

    public void handlePlayerMoveRight() {
        objectManager.getPlayer().moveRight();
        turnProcessor.advanceTurn();
    }

    public void handlePlayerRotate() {
        objectManager.getPlayer().rotate();
        turnProcessor.advanceTurn();
    }

    public void handlePlayerAttack(AttackType type) {
        objectManager.getPlayer().tryAddAttack(type.ordinal(), System.currentTimeMillis());
        turnProcessor.advanceTurn();
    }

    public void incrementTurnCount() {
        turnCount++;
        turnAnimStartTime = System.currentTimeMillis();
        turnAnimActive = true;
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

        if (turnAnimActive) {
            long elapsed = System.currentTimeMillis() - turnAnimStartTime;
            if (elapsed > TURN_ANIM_DURATION) {
                turnAnimActive = false;
            } else {
                float progress = elapsed / (float) TURN_ANIM_DURATION;

                float scale = 1.5f - 0.5f * progress;

                // 색상 직접 보간: YELLOW → WHITE
                int r = (int)(Color.red(Color.YELLOW) * (1 - progress) + Color.red(Color.WHITE) * progress);
                int g = (int)(Color.green(Color.YELLOW) * (1 - progress) + Color.green(Color.WHITE) * progress);
                int b = (int)(Color.blue(Color.YELLOW) * (1 - progress) + Color.blue(Color.WHITE) * progress);
                int blendedColor = Color.rgb(r, g, b);

                Paint turnPaint = new Paint();
                turnPaint.setColor(blendedColor);
                turnPaint.setTextSize(64 * scale);
                turnPaint.setTextAlign(Paint.Align.CENTER);

                canvas.drawText("Turn: " + turnCount, screenWidth / 2f, 100, turnPaint);
            }
        } else {
            Paint turnPaint = new Paint();
            turnPaint.setColor(Color.WHITE);
            turnPaint.setTextSize(64);
            turnPaint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText("Turn: " + turnCount, screenWidth / 2f, 100, turnPaint);
        }



    }

    public Player getPlayer() { return objectManager.getPlayer(); }
    public Rect getBlockRect(int index) { return objectManager.getBlockRect(index); }

    @Override public boolean onTouchEvent(MotionEvent event) { return inputHandler.handleTouch(event); }
    @Override public void onEnter() { }
    @Override public void onExit() { }
    @Override public void onPause() { }
    @Override public void onResume() { }
    @Override public boolean onBackPressed() { return false; }
}

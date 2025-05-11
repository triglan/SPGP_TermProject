package com.example.deadlockduel.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.example.deadlockduel.R;
import com.example.deadlockduel.scene.MainScene;
import com.example.deadlockduel.object.Player;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private MainScene mainScene;
    private StageManager stageManager;

    private int turnCount = 1;
    private Paint turnTextPaint;

    private float turnTextScale = 1.0f;
    private boolean isAnimatingTurnText = false;
    private int animationFrame = 0;
    private final int MAX_ANIMATION_FRAMES = 15;

    private enum TurnState { PLAYER_TURN, ENEMY_TURN }
    private TurnState turnState = TurnState.PLAYER_TURN;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        stageManager = new StageManager();
        StageConfig config = stageManager.getCurrentStage();
        mainScene = new MainScene(getResources(), width, height, config);

        turnTextPaint = new Paint();
        turnTextPaint.setColor(Color.BLUE);
        turnTextPaint.setTextSize(50);
        turnTextPaint.setTextAlign(Paint.Align.CENTER);
        turnTextPaint.setFakeBoldText(true);

//        if (context instanceof android.app.Activity) {
//            android.app.Activity activity = (android.app.Activity) context;
//            Button btnAttackExecute = activity.findViewById(R.id.btnAttackExecute);
//            if (btnAttackExecute != null) {
//                btnAttackExecute.setOnClickListener(v -> {
//                    mainScene.executeAllAttacks();  // ✅ 큐에 들어간 공격 실행!
//                });
//            }
//        }
    }

    public boolean nextStage() {
        if (stageManager.hasNext()) {
            stageManager.nextStage();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            StageConfig config = stageManager.getCurrentStage();
            mainScene = new MainScene(getResources(), width, height, config);
            turnCount = 1;
            turnState = TurnState.PLAYER_TURN;
            return true;
        }
        return false;
    }

    public void onPlayerActionFinished() {
        if (turnState != TurnState.PLAYER_TURN) return;

        turnState = TurnState.ENEMY_TURN;

        // 적의 턴 처리
        mainScene.updateEnemies();

        // 턴 증가 및 애니메이션
        nextTurn();

        // 다시 플레이어 턴으로
        turnState = TurnState.PLAYER_TURN;
    }

    public void nextTurn() {
        turnCount++;
        turnTextScale = 1.5f;
        animationFrame = 0;
        isAnimatingTurnText = true;
    }

    private void updateTurnAnimation() {
        if (!isAnimatingTurnText) return;

        animationFrame++;
        float t = animationFrame / (float) MAX_ANIMATION_FRAMES;
        turnTextScale = 1.0f + (1.5f - 1.0f) * (1 - t);

        if (animationFrame >= MAX_ANIMATION_FRAMES) {
            turnTextScale = 1.0f;
            isAnimatingTurnText = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.setRunning(false);
    }

    private class DrawThread extends Thread {
        private boolean running = false;
        private final SurfaceHolder holder;

        public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        synchronized (holder) {
                            drawGame(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void drawGame(Canvas canvas) {
        mainScene.update(); // 플레이어만 업데이트
        updateTurnAnimation();
        mainScene.draw(canvas);

        int centerX = getWidth() / 2;
        int topMargin = 80;

        canvas.save();
        canvas.scale(turnTextScale, turnTextScale, centerX, topMargin);
        canvas.drawText("Turn : " + turnCount, centerX, topMargin, turnTextPaint);
        canvas.restore();
    }

    public void playEffect(AttackType type) {
        mainScene.performAttack(type);  // AttackCommand.execute() → enemy.hit() + effect 출력
        int direction = mainScene.getPlayer().getDirection();
        int playerIndex = mainScene.getPlayer().getBlockIndex();
        int targetBlock = playerIndex + direction;

        if (targetBlock < 0 || targetBlock >= mainScene.getBlockCount()) return;

        Rect rect = mainScene.getBlockRect(targetBlock);
        int effectX = rect.centerX() - type.effectFrames[0].getWidth() / 2;
        int effectY = rect.centerY() - type.effectFrames[0].getHeight() / 2 + type.offsetY;
        boolean finalFacingRight = (direction == 1) == type.effectFacesRight;

        mainScene.getEffects().add(
                new com.example.deadlockduel.framework.AttackEffect(
                        type.effectFrames,
                        effectX,
                        effectY,
                        finalFacingRight,
                        false
                )
        );
    }


    public Player getPlayer() {
        return this.mainScene.getPlayer();
    }

    public int getBlockCount() {
        return this.mainScene.getBlockCount();
    }
}

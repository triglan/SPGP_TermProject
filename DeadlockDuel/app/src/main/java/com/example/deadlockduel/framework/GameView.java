package com.example.deadlockduel.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

        // 턴 텍스트 스타일
        turnTextPaint = new Paint();
        turnTextPaint.setColor(Color.BLUE);
        turnTextPaint.setTextSize(50);
        turnTextPaint.setTextAlign(Paint.Align.CENTER);
        turnTextPaint.setFakeBoldText(true);
    }

    public boolean nextStage() {
        if (stageManager.hasNext()) {
            stageManager.nextStage();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            StageConfig config = stageManager.getCurrentStage();
            mainScene = new MainScene(getResources(), width, height, config);
            turnCount = 1;
            return true;
        }
        return false;
    }

    public void nextTurn() {
        turnCount++;
        turnTextScale = 1.5f;           // 텍스트 확대 시작
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

    public Player getPlayer() {
        return this.mainScene.getPlayer();
    }

    public int getBlockCount() {
        return this.mainScene.getBlockCount();
    }

    public void onPlayerActionFinished() {
        // TODO: 적 행동 먼저 실행
        // ex) enemyManager.updateEnemies();

        nextTurn(); // 적도 다 했으면 턴 증가
    }

    private void drawGame(Canvas canvas) {
        mainScene.update();
        updateTurnAnimation();
        mainScene.draw(canvas);

        int centerX = getWidth() / 2;
        int topMargin = 80;

        canvas.save();
        canvas.scale(turnTextScale, turnTextScale, centerX, topMargin); // 확대 중심 기준
        canvas.drawText("Turn : " + turnCount, centerX, topMargin, turnTextPaint);
        canvas.restore();
    }
}

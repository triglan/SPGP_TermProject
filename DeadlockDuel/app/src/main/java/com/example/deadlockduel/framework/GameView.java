package com.example.deadlockduel.framework;

import android.content.Context;
import android.graphics.Canvas;
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

        stageManager = new StageManager(); // ✅ 1~3스테이지 담긴 매니저
        StageConfig config = stageManager.getCurrentStage();
        mainScene = new MainScene(getResources(), width, height, config);

        mainScene = new MainScene(getResources(), width, height, config);
    }

    public boolean nextStage() {
        if (stageManager.hasNext()) {
            stageManager.nextStage();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            StageConfig config = stageManager.getCurrentStage();
            mainScene = new MainScene(getResources(), width, height, config);
            return true;
        }
        return false;
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

    private void drawGame(Canvas canvas) {
        mainScene.update();
        mainScene.draw(canvas);
    }
}

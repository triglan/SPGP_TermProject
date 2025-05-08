package com.example.deadlockduel.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.deadlockduel.scene.MainScene;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private MainScene mainScene;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        // 임시 크기, 실제 onSizeChanged에서 Metrics 적용하면 좋음
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        mainScene = new MainScene(getResources(), width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

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

    private void drawGame(Canvas canvas) {
        mainScene.draw(canvas);  // 🔥 핵심: MainScene에 그리기 위임
    }
}

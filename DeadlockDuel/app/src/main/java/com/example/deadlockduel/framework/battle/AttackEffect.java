package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class AttackEffect {
    private final Bitmap[] frames;
    private final int x, y;
    private final boolean facingRight;
    private final boolean enlarged;
    private final int totalDurationFrames = 30; // 약 0.5초 @60fps
    private final int frameInterval;
    private int frameIndex = 0;
    private int frameTick = 0;

    public AttackEffect(Bitmap[] frames, int x, int y, boolean facingRight, boolean enlarged) {
        this.frames = frames;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        this.enlarged = enlarged;
        this.frameInterval = totalDurationFrames / frames.length;
    }

    public void update() {
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            frameIndex++;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (frameIndex >= frames.length) return;

        Bitmap frame = frames[frameIndex];
        int cx = x + frame.getWidth() / 2;
        int cy = y + frame.getHeight() / 2;

        float scale = enlarged ? 1.3f : 1f;

        canvas.save();
        canvas.scale(
                facingRight ? scale : -scale,
                scale,
                cx,
                cy
        );
        canvas.drawBitmap(frame, x, y, paint); // y는 이미 offset이 반영된 값으로 들어옴
        canvas.restore();
    }

    public boolean isDone() {
        return frameIndex >= frames.length;
    }
}

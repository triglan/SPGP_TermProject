package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class AttackEffect {
    private final Bitmap[] frames;
    private final int x, y;
    private final boolean facingRight;
    private final int totalDurationFrames = 30; // 약 0.5초 @60fps
    private final int frameInterval;
    private int frameIndex = 0;
    private int frameTick = 0;
    private final float scale;


    public AttackEffect(Bitmap[] frames, int x, int y, boolean facingRight, float  scale) {
        this.frames = frames;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        this.scale = scale;
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

        int tileSize = 100; // 필요시 외부 주입 가능
        Bitmap scaledFrame = Bitmap.createScaledBitmap(frame, tileSize, tileSize, true);

        // 💡 블럭 중심에서 tileSize / 3만큼 오른쪽으로 이동
        int xOffset = tileSize / 3;
        int drawX = x + xOffset;
        int drawY = y;

        int cx = drawX + tileSize / 2;
        int cy = drawY + tileSize / 2;

        canvas.save();
        canvas.scale(
                facingRight ? scale : -scale,
                scale,
                cx,
                cy
        );
        canvas.drawBitmap(scaledFrame, drawX, drawY, paint);
        canvas.restore();
    }



    public boolean isDone() {
        return frameIndex >= frames.length;
    }
}

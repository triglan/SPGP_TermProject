package com.example.deadlockduel.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimSprite {
    private final Bitmap bitmap;
    private final int frameCount;
    private final int frameWidth;
    private final int frameHeight;
    private final int startWidth;  // 시작 가로
    private final int startHeight;  // 시작 세로
    private final int scale;
    private int currentFrame = 0;
    private long lastTime = 0;
    private final long frameDelay = 150;


    public AnimSprite(Context context, int resId, int frameCount, int frameWidth,
                      int frameHeight, int startWidth, int startHeight, int scale) {
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.startWidth = startWidth;
        this.startHeight = startHeight;
        this.scale = scale;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public void update() {
        if (System.currentTimeMillis() - lastTime > frameDelay) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastTime = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas, int x, int y) {
        int srcX = startWidth * frameWidth;                      // 열 고정
        int srcY = (startHeight + currentFrame) * frameHeight;   // 행 증가
        Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY + frameHeight);
        Rect dst = new Rect(x, y, x + frameWidth * scale, y + frameHeight * scale);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }
}

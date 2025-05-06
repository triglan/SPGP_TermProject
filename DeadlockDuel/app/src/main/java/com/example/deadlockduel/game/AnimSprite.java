package com.example.deadlockduel.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.deadlockduel.R;

public class AnimSprite {
    private Bitmap bitmap;
    private Rect srcRect, dstRect;
    private int frameCount = 4;
    private int currentFrame = 0;
    private int frameWidth, frameHeight;
    private long lastFrameTime = 0;
    private long frameDelay = 150; // 밀리초

    public AnimSprite(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        frameWidth = bitmap.getWidth() / 12;  // 전체가 12칸짜리 시트
        frameHeight = bitmap.getHeight() / 9; // 9줄 중 첫 줄

        dstRect = new Rect(300, 500, 300 + frameWidth * 3, 500 + frameHeight * 3); // 확대 위치
    }

    public void update() {
        if (System.currentTimeMillis() - lastFrameTime > frameDelay) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastFrameTime = System.currentTimeMillis();
        }

        int left = currentFrame * frameWidth;
        int top = 0;
        srcRect = new Rect(left, top, left + frameWidth, top + frameHeight);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
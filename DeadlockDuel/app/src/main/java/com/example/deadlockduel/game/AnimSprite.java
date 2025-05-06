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
    private int frameWidth = 67, frameHeight = 63, frameCount = 4;
    private int currentFrame = 0;
    private long lastTime = 0;
    private long frameDelay = 150;

    public AnimSprite(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        int drawX = 600, drawY = 600; // 원하는 위치
        int scale = 3; // 크기 3배 확대
        dstRect = new Rect(drawX, drawY, drawX + frameWidth * scale,
                drawY + frameHeight * scale);

    }

    public void update() {
        if (System.currentTimeMillis() - lastTime > frameDelay) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastTime = System.currentTimeMillis();
        }

        int left = currentFrame * frameWidth;
        srcRect = new Rect(left, 0, left + frameWidth, frameHeight);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}

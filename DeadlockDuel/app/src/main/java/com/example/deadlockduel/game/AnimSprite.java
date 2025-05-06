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
    private int frameWidth, frameHeight;
    private int currentFrame = 0;
    private int frameCount = 4;
    private long lastTime = 0;
    private long frameDelay = 150;

    public AnimSprite(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        frameWidth = bitmap.getWidth() / frameCount;
        frameHeight = bitmap.getHeight();
        dstRect = new Rect(300, 300, 300 + frameWidth * 3, 300 + frameHeight * 3);
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

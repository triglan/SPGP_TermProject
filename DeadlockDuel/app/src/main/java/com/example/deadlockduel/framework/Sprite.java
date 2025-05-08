package com.example.deadlockduel.framework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    private final Bitmap bitmap;
    private final int frameCount;
    private final boolean isVertical;
    private final int frameWidth, frameHeight;
    private int frameIndex = 0;
    private final float scale;

    public Sprite(Resources res, int resId, int frameCount, boolean isVertical,
                  int frameWidth, int frameHeight, float scale) {
        this.frameCount = frameCount;
        this.isVertical = isVertical;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.scale = scale;

        int totalWidth = isVertical ? frameWidth : frameWidth * frameCount;
        int totalHeight = isVertical ? frameHeight * frameCount : frameHeight;

        this.bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, resId),
                totalWidth,
                totalHeight,
                false
        );
    }

    public void setFrame(int index) {
        this.frameIndex = index % frameCount;
    }

    public void draw(Canvas canvas, int x, int y) {
        int drawWidth = (int)(frameWidth * scale);
        int drawHeight = (int)(frameHeight * scale);

        int srcX = isVertical ? 0 : frameIndex * frameWidth;
        int srcY = isVertical ? frameIndex * frameHeight : 0;

        Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY + frameHeight);
        Rect dst = new Rect(x, y, x + drawWidth, y + drawHeight);

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public int getWidth() {
        return frameWidth;
    }

    public int getHeight() {
        return frameHeight;
    }

    public float getScale() {
        return scale;
    }
}

package com.example.deadlockduel.framework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;

public class SpriteFrames {
    private final Bitmap[] frames;
    private final float scale;
    private final int offsetX, offsetY;
    private int frameIndex = 0;

    public SpriteFrames(Resources res, int[] resIds, float scale, int offsetX, int offsetY) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.frames = new Bitmap[resIds.length];

        for (int i = 0; i < resIds.length; i++) {
            Bitmap raw = BitmapFactory.decodeResource(res, resIds[i]);
            frames[i] = Bitmap.createScaledBitmap(
                    raw,
                    (int)(raw.getWidth() * scale),
                    (int)(raw.getHeight() * scale),
                    false
            );
        }
    }

    public void setFrame(int index) {
        this.frameIndex = index % frames.length;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public float getScale() {
        return scale;
    }

    public void draw(Canvas canvas, int x, int y) {
        Bitmap frame = frames[frameIndex];
        canvas.drawBitmap(
                frame,
                x + (int)(offsetX * scale),
                y + (int)(offsetY * scale),
                null
        );
    }
    public void draw(Canvas canvas, int x, int y, Paint paint) {
        Bitmap frame = frames[frameIndex];
        canvas.drawBitmap(
                frame,
                x + (int) (offsetX * scale),
                y + (int) (offsetY * scale),
                paint
        );
    }

    public int getWidth() {
        return frames[0].getWidth();
    }

    public int getHeight() {
        return frames[0].getHeight();
    }
}

package com.example.deadlockduel.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block {
    private final Rect rect;
    private boolean hasPlayer = false;
    private boolean hasEnemy = false;
    private boolean willBeAttacked = false;

    public Block(Rect rect) {
        this.rect = rect;
    }

    public void setHasPlayer(boolean value) { this.hasPlayer = value; }
    public void setHasEnemy(boolean value) { this.hasEnemy = value; }
    public void setWillBeAttacked(boolean value) { this.willBeAttacked = value; }

    public void reset() {
        hasPlayer = false;
        hasEnemy = false;
        willBeAttacked = false;
    }

    public Rect getRect() {
        return rect;
    }
    public boolean hasEnemy() {
        return hasEnemy;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (willBeAttacked) {
            paint.setColor(Color.YELLOW);
        } else if (hasPlayer) {
            paint.setColor(Color.GREEN);
        } else if (hasEnemy) {
            paint.setColor(Color.BLUE);
        } else {
            paint.setColor(Color.GRAY);
        }


        canvas.drawRect(rect, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(rect, paint);
        paint.setStyle(Paint.Style.FILL);
    }
}

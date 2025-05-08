package com.example.deadlockduel.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block {
    private final Rect rect;
    private int color;

    public Block(Rect rect, int color) {
        this.rect = rect;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas, Paint paint) {
        // 내부 채우기
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect(rect, paint);

        // 테두리
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(rect, paint);

        // 상태 초기화
        paint.setStyle(Paint.Style.FILL);
    }

    public Rect getRect() {
        return rect;
    }

    public int getColor() {
        return color;
    }
}

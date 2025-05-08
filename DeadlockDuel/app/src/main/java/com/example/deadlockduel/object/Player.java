package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.Sprite;

public class Player {
    private final Sprite sprite;
    private int blockIndex = 0;
    private int drawX, drawY;

    public Player(Resources res) {
        sprite = new Sprite(res, R.drawable.player, 4, true, 100, 100, 3.0f);
    }

    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void updatePositionFromBlock(Rect blockRect) {
        int x = blockRect.centerX();
        int y = blockRect.top;

        int drawWidth = (int)(sprite.getWidth() * sprite.getScale());
        int drawHeight = (int)(sprite.getHeight() * sprite.getScale());

        drawX = x - drawWidth / 2;
        drawY = y - drawHeight;
    }

    public void update() {
        // 프레임 애니메이션 등
    }

    public void draw(Canvas canvas) {
        sprite.draw(canvas, drawX, drawY);
    }
}

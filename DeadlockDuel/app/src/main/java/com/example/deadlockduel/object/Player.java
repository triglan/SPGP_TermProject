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

    private int frameTick = 0;
    private final int frameInterval = 8;

    public Player(Resources res) {
        float scale = 3f;
        int offsetX = 0;
        int offsetY = 47 + 5; // 하단 공백 보정값 (이미지에 따라 조정)
        int startY = 47;
        sprite = new Sprite(res, R.drawable.player,
                4, true, 100, 128,
                scale, offsetX, offsetY, startY);

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
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            sprite.setFrame(sprite.getFrameIndex() + 1);
        }
    }

    public void draw(Canvas canvas) {
        sprite.draw(canvas, drawX, drawY);
    }
}

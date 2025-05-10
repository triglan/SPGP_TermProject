package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;

public class Player {
    private final SpriteFrames sprite;
    private int blockIndex = 0;
    private int drawX, drawY;
    private int direction = 1; // 1 = 오른쪽, -1 = 왼쪽
    private static final int TILE_WIDTH = 100;
    private boolean facingRight = true;
    private int frameTick = 0;
    private final int frameInterval = 8;

    private int blockCount;

    public Player(Resources res) {
        int[] resIds = {
                R.drawable.player_idle_1,
                R.drawable.player_idle_2,
                R.drawable.player_idle_3,
                R.drawable.player_idle_4
        };
        sprite = new SpriteFrames(res, resIds, 1.0f, 0, 0);
    }

    public void setBlockCount(int count) {
        this.blockCount = count;
    }

    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void updateBlockState(Block[] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setHasPlayer(i == blockIndex);
        }
    }

    public void updatePositionFromBlock(Rect blockRect) {
        int drawWidth = sprite.getWidth();
        int drawHeight = sprite.getHeight();
        drawX = blockRect.centerX() - drawWidth / 2;
        drawY = blockRect.top - drawHeight;
    }

    public void moveLeft() {
        if (blockIndex > 0) {
            blockIndex--;
            Log.d("Player", "← 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "왼쪽 끝이라 이동 불가");
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            Log.d("Player", "→ 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "오른쪽 끝이라 이동 불가");
        }
    }

    public void reset(int blockIndex, boolean faceRight) {
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
    }

    public void rotate() {
        this.direction *= -1;
        this.facingRight = (direction == 1);
        Log.d("Player", "⟳ 회전: direction = " + direction);
    }

    public void update() {
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            sprite.setFrame(sprite.getFrameIndex() + 1);
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        if (facingRight) {
            canvas.scale(-1, 1, drawX + sprite.getWidth() / 2f, drawY + sprite.getHeight() / 2f);
        }
        sprite.draw(canvas, drawX, drawY);
        canvas.restore();
    }
}

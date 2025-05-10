package com.example.deadlockduel.object;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.deadlockduel.framework.SpriteFrames;

public abstract class Enemy {
    protected SpriteFrames sprite;
    protected int blockIndex = 0;
    protected int drawX, drawY;
    protected int direction = 1;
    protected static final int TILE_WIDTH = 100;
    protected boolean facingRight = true;
    protected int frameTick = 0;
    protected final int frameInterval = 8;
    protected int blockCount;
    protected boolean isDead = false;
    protected int hp;

    public Enemy() { }

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
        if (blockIndex >= 0 && blockIndex < blocks.length) {
            blocks[blockIndex].setHasEnemy(true);
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
            Log.d("Enemy", "← 이동: blockIndex = " + blockIndex);
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            Log.d("Enemy", "→ 이동: blockIndex = " + blockIndex);
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
        Log.d("Enemy", "⟳ 회전: direction = " + direction);
    }

    public abstract void act(Player player, Enemy[] enemies);

    public void updateAnimation() {
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            if (sprite != null) {
                sprite.setFrame(sprite.getFrameIndex() + 1);
            }
        }
    }

    public void draw(Canvas canvas) {
        if (sprite == null || isDead) return;
        canvas.save();
        if (facingRight) {
            canvas.scale(-1, 1, drawX + sprite.getWidth() / 2f, drawY + sprite.getHeight() / 2f);
        }
        sprite.draw(canvas, drawX, drawY);
        canvas.restore();
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill() {
        this.isDead = true;
    }

    public void setSprite(SpriteFrames sprite) {
        this.sprite = sprite;
    }

    public SpriteFrames getSprite() {
        return sprite;
    }
}

package com.example.deadlockduel.object;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.deadlockduel.framework.SpriteFrames;

public class Enemy {
    protected SpriteFrames sprite;
    protected int blockIndex = 0;
    protected int drawX, drawY;
    protected int direction = -1; // -1: 왼쪽, 1: 오른쪽
    protected static final int TILE_WIDTH = 100;
    protected boolean flipped = true;
    protected int frameTick = 0;
    protected final int frameInterval = 8;
    protected int blockCount;
    protected boolean isDead = false;
    protected int hp;

    public Enemy() {
        // 자식 클래스에서 sprite 초기화 필요
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
        if (blockIndex >= 0 && blockIndex < blocks.length) {
            blocks[blockIndex].setHasEnemy(true); // ✅ 현재 적 위치만 true
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
        } else {
            Log.d("Enemy", "왼쪽 끝이라 이동 불가");
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            Log.d("Enemy", "→ 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Enemy", "오른쪽 끝이라 이동 불가");
        }
    }

    public void reset(int blockIndex, boolean faceRight) {
        this.blockIndex = blockIndex;
        this.direction = faceRight ? -1 : 1;
        this.flipped = faceRight;
    }

    public void rotate() {
        this.direction *= -1;
        setFlipped(direction == -1);
        Log.d("Enemy", "⟳ 회전: direction = " + direction);
    }

    private void setX(int x) {
        this.drawX = x;
    }

    private void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void act(Player player) {
        // 기본 구현은 없음 (자식 클래스에서 오버라이드)
    }

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
        if (flipped) {
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

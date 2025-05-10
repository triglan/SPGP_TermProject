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
    private static final int TILE_WIDTH = 100; // 블록 간 간격 (게임 규칙에 맞게 조정)
    private boolean flipped = false; // 현재 시점의 방향 (시각적 반전)
    private int frameTick = 0;
    private final int frameInterval = 8;

    private int blockCount; // 외부(MainScene 등)에서 전달받는 총 블럭 수

    public Player(Resources res) {
        int[] resIds = {
                R.drawable.player_idle_1,
                R.drawable.player_idle_2,
                R.drawable.player_idle_3,
                R.drawable.player_idle_4
        };
        sprite = new SpriteFrames(res, resIds, 1.0f, 0, 0);
    }

    // 외부에서 블럭 총 개수를 전달받음 (필수)
    public void setBlockCount(int count) {
        this.blockCount = count;
    }

    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    // 현재 블럭 상태 업데이트 (MainScene에서 호출)
    public void updateBlockState(Block[] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setHasPlayer(i == blockIndex);
        }
    }

    // 실제 화면상의 위치 갱신
    public void updatePositionFromBlock(Rect blockRect) {
        int drawWidth = sprite.getWidth();
        int drawHeight = sprite.getHeight();
        drawX = blockRect.centerX() - drawWidth / 2;
        drawY = blockRect.top - drawHeight;
    }

    public void moveLeft() {
        if (blockIndex > 0) {
            blockIndex--;
            setX(blockIndex * TILE_WIDTH);
            Log.d("Player", "← 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "왼쪽 끝이라 이동 불가");
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            setX(blockIndex * TILE_WIDTH);
            Log.d("Player", "→ 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "오른쪽 끝이라 이동 불가");
        }
    }

    public void reset(int blockIndex, boolean faceRight) {
        this.blockIndex = blockIndex;
        this.direction = faceRight ? -1 : 1;
        this.flipped = faceRight; // true면 왼쪽 보게
    }

    public void rotate() {
        this.direction *= -1;
        setFlipped(direction == -1);
        Log.d("Player", "⟳ 회전: direction = " + direction);
    }

    private void setX(int x) {
        this.drawX = x;
    }

    private void setFlipped(boolean flipped) {
        this.flipped = flipped;
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

        if (flipped) {
            canvas.scale(-1, 1, drawX + sprite.getWidth() / 2f, drawY + sprite.getHeight() / 2f);
        }

        sprite.draw(canvas, drawX, drawY);

        canvas.restore();
    }
}

package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackEffect;
import com.example.deadlockduel.framework.battle.AttackType;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class Player {
    private final SpriteFrames sprite;
    private int blockIndex = 0;
    private int drawX, drawY;
    private int direction = 1; // 1 = 오른쪽, -1 = 왼쪽
    private static final int TILE_WIDTH = 100;
    private boolean facingRight = true;
    private int frameTick = 0;
    private final int frameInterval = 8;
    private int hp = 10, maxHp = 10;

    private int blockCount;

    // 공격 대기열 및 쿨타임 관련
    private final Queue<AttackCommand> attackQueue = new LinkedList<>();
    private final int[] weaponCooldowns = new int[] {3, 3, 3};
    private final int[] weaponCooldownMax = new int[] {2, 3, 3};

    public Player(Resources res) {
        int[] resIds = {
                R.drawable.player_idle_1,
                R.drawable.player_idle_2,
                R.drawable.player_idle_3,
                R.drawable.player_idle_4
        };
        sprite = new SpriteFrames(res, resIds, 1.0f, 0, 0);
    }

    public void setBlockCount(int count) { this.blockCount = count; }
    public void setBlockIndex(int index) { this.blockIndex = index; }
    public int getBlockIndex() { return blockIndex; }
    public int getDirection() { return direction; }

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
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            Log.d("Player", "→ 이동: blockIndex = " + blockIndex);
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

        // HP 및 체력바
        Paint hpPaint = new Paint();
        hpPaint.setColor(Color.GREEN);
        hpPaint.setTextAlign(Paint.Align.CENTER);
        hpPaint.setTextSize(28f);
        canvas.drawText("HP: " + this.hp, drawX + sprite.getWidth() / 2f, drawY - 25, hpPaint);

        int barWidth = sprite.getWidth();
        int barHeight = 8;
        int barX = drawX;
        int barY = drawY - 16;

        Paint backPaint = new Paint();
        backPaint.setColor(Color.RED);
        canvas.drawRect(barX, barY, barX + barWidth, barY + barHeight, backPaint);

        hpPaint.setColor(Color.GREEN);
        float ratio = (float) hp / maxHp;
        canvas.drawRect(barX, barY, barX + (int)(barWidth * ratio), barY + barHeight, hpPaint);
    }

    // ✅ 공격 대기열 관련
    public boolean tryEnqueueAttack(int weaponIndex, boolean perfectTiming) {
        if (weaponCooldowns[weaponIndex] < weaponCooldownMax[weaponIndex]) {
            return false; // 쿨타임 중
        }
        AttackType type = AttackType.getByIndex(weaponIndex);
        AttackCommand command = new AttackCommand(type, this, perfectTiming);
        attackQueue.offer(command);
        weaponCooldowns[weaponIndex] = 0;
        return true;
    }

    public void executeNextAttack(Enemy[] enemies, List<AttackEffect> effects, Block[] blocks) {
        if (attackQueue.isEmpty()) return;
        AttackCommand command = attackQueue.poll();
        command.execute(enemies, effects, this, blocks);
    }

    public void advanceCooldowns() {
        for (int i = 0; i < weaponCooldowns.length; i++) {
            if (weaponCooldowns[i] < weaponCooldownMax[i]) {
                weaponCooldowns[i]++;
            }
        }
    }

    public int getCooldown(int weaponIndex) {
        return weaponCooldowns[weaponIndex];
    }

    public int getCooldownMax(int weaponIndex) {
        return weaponCooldownMax[weaponIndex];
    }

    public boolean hasPendingAttack() {
        return !attackQueue.isEmpty();
    }
}

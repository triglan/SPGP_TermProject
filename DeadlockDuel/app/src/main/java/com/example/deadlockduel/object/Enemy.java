package com.example.deadlockduel.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import java.util.List;
import com.example.deadlockduel.framework.battle.AttackCommand;

public abstract class Enemy {
    protected SpriteFrames sprite;
    protected int blockIndex = 0;
    protected int drawX, drawY;
    protected int direction = 1;
    protected boolean facingRight = true;
    protected int frameTick = 0;
    protected final int frameInterval = 8;
    protected int blockCount;
    protected boolean isDead = false;

    protected int hp = 5;
    protected int maxHp = 5;
    protected int attackPower = 1;

    // ✅ 피격 연출용 변수
    protected int hitFlashTimer = 0;
    protected int hitShakeTimer = 0;

    public void setBlockCount(int count) {
        this.blockCount = count;
    }

    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setHp(int hp) {
        this.hp = hp;
        this.maxHp = hp;
    }

    public int getHp() {
        return hp;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setAttackPower(int power) {
        this.attackPower = power;
    }

    public int getAttackPower() {
        return this.attackPower;
    }


    public int getMaxHp() {
        return maxHp;
    }

    public void hit(int damage) {
        if (isDead) return;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            isDead = true;
        }
        hitFlashTimer = 18;
        hitShakeTimer = 18;
    }
    public void takeDamage(int damage) {
        hit(damage);
    }

    public void setDirection(int dir) {
        this.direction = dir;
        this.facingRight = (dir == 1);
    }


    public void updateBlockState(Block[] blocks) {
        if (blockIndex >= 0 && blockIndex < blocks.length) {
            blocks[blockIndex].setHasEnemy(!isDead);
        }
    }

    public void updatePositionFromBlock(Rect blockRect) {
        int drawWidth = sprite.getWidth();
        int drawHeight = sprite.getHeight();
        drawX = blockRect.centerX() - drawWidth / 2;
        drawY = blockRect.top - drawHeight;
    }

    public void moveLeft() {
        if (blockIndex > 0) blockIndex--;
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) blockIndex++;
    }

    public void reset(int blockIndex, boolean faceRight) {
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
    }

    public void rotate() {
        this.direction *= -1;
        this.facingRight = (direction == 1);
    }

    public abstract void act(Player player, Enemy[] enemies, List<AttackCommand> attackQueue);

    public Rect getCurrentDrawRect() {
        return new Rect(drawX, drawY, drawX + sprite.getWidth(), drawY + sprite.getHeight());
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

        // ✅ 흔들림 처리
        // int shakeOffset = (hitShakeTimer > 0) ? ((hitShakeTimer % 2 == 0) ? -6 : 6) : 0;
        int shakeOffset = 0;
        if (hitShakeTimer > 0) {
            int shakePhase = (hitShakeTimer / 4) % 2;
            shakeOffset = (shakePhase == 0) ? -16 : 16;
        }
        canvas.translate(shakeOffset, 0);

        // 방향 처리
        if (facingRight) {
            canvas.scale(-1, 1, drawX + sprite.getWidth() / 2f, drawY + sprite.getHeight() / 2f);
        }

        // ✅ 반짝임 처리 + 깜빡이는 반짝임 처리 (hitFlashTimer가 짝수일 때만 흰색 출력)
        if (hitFlashTimer > 0) {
            if ((hitFlashTimer / 2) % 2 == 0) {
                Paint flashPaint = new Paint();
                flashPaint.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
                sprite.draw(canvas, drawX, drawY, flashPaint);
            } else {
                sprite.draw(canvas, drawX, drawY); // 일반 출력
            }
            hitFlashTimer--;
        } else {
            sprite.draw(canvas, drawX, drawY); // 기본 상태
        }
        canvas.restore();

        if (hitShakeTimer > 0) hitShakeTimer--;

        Paint hpPaint = new Paint();
        hpPaint.setColor(Color.GREEN);
        hpPaint.setTextAlign(Paint.Align.CENTER);
        hpPaint.setTextSize(28f);
        canvas.drawText("HP: " + this.hp, drawX + sprite.getWidth() / 2f, drawY - 25, hpPaint);
        // ✅ HP 바 그리기
        Paint backPaint = new Paint();
        backPaint.setColor(Color.RED);
        hpPaint.setColor(Color.GREEN);

        int barWidth = sprite.getWidth();
        int barHeight = 8;
        int barX = drawX;
        int barY = drawY - 16;

        canvas.drawRect(barX, barY, barX + barWidth, barY + barHeight, backPaint);
        float ratio = (float) hp / maxHp;
        canvas.drawRect(barX, barY, barX + (int)(barWidth * ratio), barY + barHeight, hpPaint);
    }

    public boolean isDead() {
        return isDead;
    }

    public SpriteFrames getSprite() {
        return sprite;
    }

    public void setSprite(SpriteFrames sprite) {
        this.sprite = sprite;
    }
}

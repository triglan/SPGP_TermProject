package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.battle.EnemyAttackCommand;

public class Enemy_Knight extends Enemy {
    int alertLevel = 0;
    public Enemy_Knight(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
        setHp(10); // 기사 체력 10

        int[] resIds = {
                R.drawable.knight_idle_1,
                R.drawable.knight_idle_2,
                R.drawable.knight_idle_3,
                R.drawable.knight_idle_4
        };
        setSprite(new SpriteFrames(res, resIds, 0.8f, 0, 3));
    }

    @Override
    public void act(Player player, Enemy[] enemies, List<AttackCommand> attackQueue) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        int dist = playerIndex - this.blockIndex;

        // 1. 방향 회전
        if ((dist < 0 && direction != -1) || (dist > 0 && direction != 1)) {
            this.direction = (dist < 0) ? -1 : 1;
            return;
        }

        int frontIndex = this.blockIndex + direction;

        // 5. 느낌표 3개 → 플레이어가 여전히 정면 → 공격
        if (alertLevel == 2 && playerIndex == frontIndex) {
            attackQueue.add(new EnemyAttackCommand(AttackType.POWER, this, player));
            alertLevel = 0;
            return;
        }

        // 4. 이미 느낌표 1개 상태 → 정면 유지 중이면 3개로 승격
        if (alertLevel == 1 && playerIndex == frontIndex) {
            alertLevel = 2;
            return;
        }

        // 3. 플레이어 정면에 있으면 느낌표 1개 띄움
        if (playerIndex == frontIndex) {
            alertLevel = 1;
            return;
        }

        // 2. 정면까지 최대 1칸 이동 (중간 적 피하기)
        int nextIndex = this.blockIndex + direction;

        boolean blocked = false;
        for (Enemy e : enemies) {
            if (e != this && !e.isDead() && e.getBlockIndex() == nextIndex) {
                blocked = true;
                break;
            }
        }
        if (!blocked && nextIndex != playerIndex) {
            this.blockIndex = nextIndex;
        }

        alertLevel = 0;  // 위치 바뀌면 대기 상태 리셋
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!isDead() && alertLevel > 0) {
            Rect body = getCurrentDrawRect();

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(48);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.CENTER);

            String alertText = (alertLevel == 1) ? "!" : "!!!";
            canvas.drawText(alertText, body.centerX(), body.top - 20, paint);
        }
    }

}

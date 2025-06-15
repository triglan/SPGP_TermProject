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

public class Enemy_Archer extends Enemy {
    private int warnedIndex = -1;
    private boolean resting = false;
    private boolean alerted = false; // 머리 위 느낌표 표시 여부


    public Enemy_Archer(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
        setHp(5); // 아쳐 체력 5

        int[] resIds = {
                R.drawable.archer_idle_1,
                R.drawable.archer_idle_2,
                R.drawable.archer_idle_3,
                R.drawable.archer_idle_4
        };
        setSprite(new SpriteFrames(res, resIds, 0.8f, 0, 0));
    }

    @Override
    public void act(Player player, Enemy[] enemies, List<AttackCommand> attackQueue) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        int dist = playerIndex - this.blockIndex;

        // 1. 방향만 맞추기
        if ((dist < 0 && direction != -1) || (dist > 0 && direction != 1)) {
            this.direction = (dist < 0) ? -1 : 1;
            return;
        }

        // 2. 발사 턴
        if (warnedIndex >= 0) {
            // 발사: 실제 데미지는 player에게, 이펙트는 warnedIndex에 출력
            EnemyAttackCommand cmd = new EnemyAttackCommand(AttackType.LONG_RANGE, this, player);
            cmd.overrideEffectPosition(warnedIndex);  // 🔥 커스텀 위치 지정
            attackQueue.add(cmd);

            warnedIndex = -1;
            alerted = false;
            resting = true;
            return;
        }

        // 3. 쉬는 턴
        if (resting) {
            resting = false;
            return;
        }

        // 4. 예고 턴
        warnedIndex = playerIndex;
        alerted = true;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!isDead() && alerted) {
            Rect body = getCurrentDrawRect();

            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setTextSize(100);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText("!", body.centerX(), body.top - 20, paint);
        }
    }



}

package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.battle.EnemyAttackCommand;

public class
Enemy_Rogue extends Enemy {
    private boolean readyToAttack = false;
    private long attackStartTime = 0;
    private boolean shaking = false;
    private boolean alerted = false; // 느낌표가 떠 있는 상태
    public Enemy_Rogue(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
        setHp(5);
        setAttackPower(3);

        int[] resIds = {
                R.drawable.rogue_idle_1,
                R.drawable.rogue_idle_2,
                R.drawable.rogue_idle_3,
                R.drawable.rogue_idle_4
        };
        setSprite(new SpriteFrames(res, resIds, 0.8f, 0, 0));
    }

    @Override
    public void act(Player player, Enemy[] enemies, List<AttackCommand> attackQueue) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        int dist = playerIndex - this.blockIndex;

        // 1. 플레이어를 바라보도록 회전
        if ((dist < 0 && direction != -1) || (dist > 0 && direction != 1)) {
            this.direction = (dist < 0) ? -1 : 1;
            return;
        }

        int frontIndex = this.blockIndex + direction;

        // 4. 느낌표 떠 있고 정면에 플레이어 있으면 공격
        if (alerted && playerIndex == frontIndex) {
            attackQueue.add(new EnemyAttackCommand(AttackType.MELEE, this, player));
            alerted = false;
            return;
        }

        // 3. 정면에 플레이어 있으면 느낌표 표시
        if (playerIndex == frontIndex) {
            alerted = true;
            return;
        }

        // 2. 플레이어 정면까지 최대 2칸 이동 (중간 Enemy 피하기)
        for (int i = 0; i < 2; i++) {
            int nextIndex = this.blockIndex + direction;

            boolean blocked = false;
            for (Enemy e : enemies) {
                if (e != this && !e.isDead() && e.getBlockIndex() == nextIndex) {
                    blocked = true;
                    break;
                }
            }
            if (blocked || nextIndex == playerIndex) break;

            this.blockIndex = nextIndex;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!isDead() && alerted) {
            Rect body = getCurrentDrawRect();

            Paint paint = new Paint();
            paint.setColor(Color.RED);         // 색상
            paint.setTextSize(48);             // 글자 크기 (원하면 더 키움)
            paint.setFakeBoldText(true);       // 두껍게
            paint.setTextAlign(Paint.Align.CENTER);

            int textX = body.centerX();
            int textY = body.top - 20;         // 캐릭터 머리 위로 살짝 띄움

            canvas.drawText("!", textX, textY, paint);
        }
    }




}

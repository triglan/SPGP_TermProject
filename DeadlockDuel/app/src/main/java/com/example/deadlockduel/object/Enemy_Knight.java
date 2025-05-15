package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;

import java.util.List;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackType;

public class Enemy_Knight extends Enemy {
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

        if ((dist < 0 && direction != -1) || (dist > 0 && direction != 1)) {
            Log.d("Enemy_Knight", "회전!");
            rotate();
            return;
        }

        boolean blocked = false;
        int start = Math.min(playerIndex, blockIndex) + 1;
        int end = Math.max(playerIndex, blockIndex);
        for (Enemy e : enemies) {
            if (e == this || e.isDead()) continue;
            int idx = e.getBlockIndex();
            if (idx >= start && idx < end) {
                Log.d("Enemy_Knight", "정지!");
                blocked = true;
                break;
            }
        }
        if (blocked) return;

        if (Math.abs(dist) == 1) {
            Log.d("Enemy_Knight", "근접 공격!");
            attackQueue.add(new AttackCommand(AttackType.BASIC, player, false));
        } else if (dist > 1) {
            moveRight();
        } else if (dist < -1) {
            moveLeft();
        }
    }
}

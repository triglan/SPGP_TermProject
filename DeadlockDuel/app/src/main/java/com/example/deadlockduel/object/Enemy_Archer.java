package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;

import java.util.List;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;
import com.example.deadlockduel.framework.AttackCommand;
import com.example.deadlockduel.framework.AttackType;

public class Enemy_Archer extends Enemy {
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

        if ((dist < 0 && direction != -1) || (dist > 0 && direction != 1)) {
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
                blocked = true;
                break;
            }
        }
        if (blocked) return;

        if (Math.abs(dist) >= 1) {
            Log.d("Enemy_Archer", "원거리 공격!");
            attackQueue.add(new AttackCommand(AttackType.LONG_RANGE, player, false));
        }
    }
}

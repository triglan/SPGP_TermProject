package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;

public class Enemy_Rogue extends Enemy {
    public Enemy_Rogue(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
        this.hp = 1;

        int[] resIds = {
                R.drawable.rogue_idle_1,
                R.drawable.rogue_idle_2,
                R.drawable.rogue_idle_3,
                R.drawable.rogue_idle_4
        };
        setSprite(new SpriteFrames(res, resIds, 0.8f, 0, 0));
    }

    @Override
    public void act(Player player, Enemy[] enemies) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        if ((playerIndex < blockIndex && direction != -1) ||
                (playerIndex > blockIndex && direction != 1)) {
            rotate();
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

        if (Math.abs(playerIndex - blockIndex) == 1) {
            Log.d("Enemy_Rogue", "근접 공격!");
        }
    }
}

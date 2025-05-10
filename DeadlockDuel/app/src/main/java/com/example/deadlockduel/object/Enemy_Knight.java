package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;

public class Enemy_Knight extends Enemy {
    public Enemy_Knight(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
        this.hp = 2;

        int[] resIds = {
                R.drawable.knight_idle_1,
                R.drawable.knight_idle_2,
                R.drawable.knight_idle_3,
                R.drawable.knight_idle_4
        };
        setSprite(new SpriteFrames(res, resIds, 0.8f, 0, 3));
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

        int dist = playerIndex - blockIndex;
        if (Math.abs(dist) == 1) {
            Log.d("Enemy_Knight", "근접 공격!");
        } else if (dist > 1) {
            moveRight();
        } else if (dist < -1) {
            moveLeft();
        }
    }
}

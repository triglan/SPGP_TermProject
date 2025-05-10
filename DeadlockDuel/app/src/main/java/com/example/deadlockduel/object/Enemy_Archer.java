package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;
import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;

public class Enemy_Archer extends Enemy {
    public Enemy_Archer(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? -1 : 1;
        this.flipped = faceRight;
        this.hp = 1;

        int[] resIds = {
                R.drawable.archer_idle_1,
                R.drawable.archer_idle_2,
                R.drawable.archer_idle_3,
                R.drawable.archer_idle_4
        };
        SpriteFrames sprite = new SpriteFrames(res, resIds, 0.8f, 0, 0);
        setSprite(sprite);
    }

    @Override
    public void act(Player player) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        int dist = playerIndex - this.blockIndex;

        if (Math.abs(dist) <= 2) {
            Log.d("Enemy_Archer", "→ 원거리 공격!");
            // TODO: 공격 처리
        } else if (dist > 2) {
            moveRight();
        } else if (dist < -2) {
            moveLeft();
        }
    }
}

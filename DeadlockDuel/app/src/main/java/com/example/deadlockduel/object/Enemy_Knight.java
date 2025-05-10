package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.util.Log;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.SpriteFrames;

public class Enemy_Knight extends Enemy {
    public Enemy_Knight(Resources res, int blockIndex, boolean faceRight) {
        super();
        this.blockIndex = blockIndex;
        this.direction = faceRight ? -1 : 1;
        this.flipped = faceRight;
        this.hp = 2;

        int[] resIds = {
                R.drawable.knight_idle_1,
                R.drawable.knight_idle_2,
                R.drawable.knight_idle_3,
                R.drawable.knight_idle_4
        };
        SpriteFrames sprite = new SpriteFrames(res, resIds, 0.8f, 0, 0);
        setSprite(sprite);
    }

    @Override
    public void update(Player player) {
        if (isDead()) return;

        int playerIndex = player.getBlockIndex();
        int dist = playerIndex - this.blockIndex;

        // 1칸 차이면 공격, 아니면 이동
        if (Math.abs(dist) == 1) {
            Log.d("Enemy_Knight", "공격!");
            // TODO: 공격 구현
        } else if (dist > 1) {
            moveRight();
        } else if (dist < -1) {
            moveLeft();
        }
    }
}
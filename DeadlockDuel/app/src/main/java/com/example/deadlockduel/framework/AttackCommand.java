package com.example.deadlockduel.framework;

import android.graphics.Rect;
import android.util.Log;

import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.Player;

import java.util.List;

public class AttackCommand {
    public final AttackType type;
    public final Player player;
    public final boolean perfectTiming;

    public AttackCommand(AttackType type, Player player, boolean perfectTiming) {
        this.type = type;
        this.player = player;
        this.perfectTiming = perfectTiming;
    }

    public void execute(Enemy[] enemies, List<AttackEffect> effects, Player player, Block[] blocks) {
        int playerIndex = player.getBlockIndex();
        int direction = player.getDirection();
        int range = type.range;
        int damage = type.power + (perfectTiming ? 1 : 0);

        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;

            int dist = enemy.getBlockIndex() - playerIndex;

            // 공격 범위 안의 적만 처리
            if ((direction == 1 && dist > 0 && dist <= range) ||
                    (direction == -1 && dist < 0 && -dist <= range)) {

                enemy.hit(damage); // ✅ 타격 처리

                // 이펙트 위치 계산
                int targetBlock = enemy.getBlockIndex();
                Rect rect = blocks[targetBlock].getRect();
                int effectX = rect.centerX() - type.effectFrames[0].getWidth() / 2;
                int effectY = rect.centerY() - type.effectFrames[0].getHeight() / 2 + type.offsetY;

                boolean finalFacingRight = (direction == 1) == type.effectFacesRight;

                effects.add(new AttackEffect(
                        type.effectFrames, effectX, effectY, finalFacingRight,
                        perfectTiming));
            }
        }
    }

}

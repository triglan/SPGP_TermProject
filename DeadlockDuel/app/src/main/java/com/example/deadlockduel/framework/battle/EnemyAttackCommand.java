package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.deadlockduel.object.Enemy;
import com.example.deadlockduel.object.Player;

public class EnemyAttackCommand extends AttackCommand {
    private final Enemy attacker;
    private Integer customEffectBlockIndex = null;

    public void overrideEffectPosition(int blockIndex) {
        this.customEffectBlockIndex = blockIndex;
    }

    public EnemyAttackCommand(AttackType type, Enemy attacker, Object target) {
        super(type, null, target, false);
        this.attacker = attacker;
    }

    @Override
    public int getDamage() {
        return attacker.getAttackPower();
    }

    @Override
    public void execute() {
        if (target == null || attacker.isDead()) return;

        // 피해
        if (target instanceof Player) {
            ((Player) target).takeDamage(getDamage());
        } else if (target instanceof Enemy) {
            ((Enemy) target).takeDamage(getDamage());
        }

        // 이펙트 위치
        int effectBlockIndex = (customEffectBlockIndex != null)
                ? customEffectBlockIndex
                : (target instanceof Player ? ((Player) target).getBlockIndex()
                : ((Enemy) target).getBlockIndex());

        if (AttackCommand.blockRectProvider != null) {
            Rect rect = AttackCommand.blockRectProvider.getBlockRect(effectBlockIndex);
            Bitmap[] frames = type.effectFrames;
            boolean faceRight = type.effectFacesRight;
            int offsetY = type.offsetY;
            float scale = type.effectScale;

            int x = rect.centerX() - frames[0].getWidth() / 2;
            int y = rect.centerY() - frames[0].getHeight() / 2 + offsetY;

            EffectManager.getInstance().addEffect(
                    new AttackEffect(frames, x, y, faceRight, scale)
            );
        }
    }


}

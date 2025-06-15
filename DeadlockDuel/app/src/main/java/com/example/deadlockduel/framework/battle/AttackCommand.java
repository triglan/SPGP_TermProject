package com.example.deadlockduel.framework.battle;

import com.example.deadlockduel.object.*;
import com.example.deadlockduel.framework.core.BlockRectProvider;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.deadlockduel.framework.data.WeaponDatabase;
import com.example.deadlockduel.framework.data.WeaponInfo;

public class AttackCommand {
    public final AttackType type;
    public final Player player;
    public final Object target;
    public final boolean perfectTiming;
    public final int weaponIndex;
    public final boolean isBonus;

    // 이펙트 출력용: 외부에서 반드시 주입 필요
    public static BlockRectProvider blockRectProvider = null;

    public AttackCommand(AttackType type, Player player, Object target, boolean perfectTiming) {
        this.type = type;
        this.player = player;
        this.target = target;
        this.perfectTiming = perfectTiming;
        this.weaponIndex = -1;
        this.isBonus = perfectTiming;
    }

    public int getDamage() {
        WeaponInfo info = WeaponDatabase.get(type);
        return info.baseDamage + (perfectTiming ? 1 : 0);
    }

    public void execute() {
        if (target == null) return;

        // 1. 피해 처리
        if (target instanceof Enemy) {
            ((Enemy) target).takeDamage(getDamage());
        } else if (target instanceof Player) {
            ((Player) target).takeDamage(getDamage());
        }

        // 2. 이펙트 출력
        if (blockRectProvider != null) {
            int blockIndex = -1;
            if (target instanceof Enemy) {
                blockIndex = ((Enemy) target).getBlockIndex();
            } else if (target instanceof Player) {
                blockIndex = ((Player) target).getBlockIndex();
            }

            if (blockIndex >= 0) {
                Rect blockRect = blockRectProvider.getBlockRect(blockIndex);
                Bitmap[] frames = type.effectFrames;
                boolean faceRight = type.effectFacesRight;
                int offsetY = type.offsetY;
                float scale = type.effectScale;

                int x = blockRect.centerX() - frames[0].getWidth() / 2;
                int y = blockRect.centerY() - frames[0].getHeight() / 2 + offsetY;

                EffectManager.getInstance().playEffect(type, x, y, faceRight);
            }
        }
    }
}

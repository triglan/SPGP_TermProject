package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;

public enum AttackType {
    MELEE(1, 1, 0),
    LONG_RANGE(1, 2, 1),
    POWER(2, 2, 2);

    public final int power;
    public final int range;
    public final int cooldown;

    // 이펙트 리소스 관련 속성들
    public Bitmap[] effectFrames;
    public boolean effectFacesRight;
    public int offsetY;

    AttackType(int power, int range, int cooldown) {
        this.power = power;
        this.range = range;
        this.cooldown = cooldown;
    }
}
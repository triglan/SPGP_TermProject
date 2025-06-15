package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;

public enum AttackType {
    MELEE(1, 1, 0, 1),
    LONG_RANGE(1, 2, 1, 3),
    POWER(2, 2, 2, 1.5f);

    public final int power;
    public final int range;
    public final int cooldown;
    public final float effectScale;

    // 이펙트 리소스 관련 속성들
    public Bitmap[] effectFrames;
    public boolean effectFacesRight;
    public int offsetY;

    AttackType(int power, int range, int cooldown,  float effectScale) {
        this.power = power;
        this.range = range;
        this.cooldown = cooldown;
        this.effectScale = effectScale;
    }
}
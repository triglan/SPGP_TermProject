package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;

public enum AttackType {
    BASIC(3, 1, 2),
    LONG_RANGE(1, 99, 3),
    POWER(2, 2, 3);

    public final int power;
    public final int range;
    public final int cooldown;

    public Bitmap[] effectFrames;
    public boolean effectFacesRight;
    public int offsetY;

    AttackType(int power, int range, int cooldown) {
        this.power = power;
        this.range = range;
        this.cooldown = cooldown;
    }

    public static AttackType getByIndex(int index) {
        return values()[index];
    }
}

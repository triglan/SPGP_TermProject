package com.example.deadlockduel.framework.data;

public class WeaponInfo {
    public final int baseDamage;
    public final int maxCooldown;
    public final int range;

    public WeaponInfo(int baseDamage, int maxCooldown, int range) {
        this.baseDamage = baseDamage;
        this.maxCooldown = maxCooldown;
        this.range = range;
    }
}

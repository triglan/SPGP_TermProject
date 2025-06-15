package com.example.deadlockduel.framework.data;

import java.util.HashMap;
import java.util.Map;
import com.example.deadlockduel.framework.battle.AttackType;

public class WeaponDatabase {
    private static final Map<AttackType, WeaponInfo> weaponMap = new HashMap<>();

    static {
        weaponMap.put(AttackType.MELEE,      new WeaponInfo(4, 2, 1));
        weaponMap.put(AttackType.LONG_RANGE, new WeaponInfo(2, 3, 999));
        weaponMap.put(AttackType.POWER,      new WeaponInfo(3, 3, 2));
    }

    public static WeaponInfo get(AttackType type) {
        return weaponMap.get(type);
    }
}

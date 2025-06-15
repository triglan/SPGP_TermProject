package com.example.deadlockduel.framework.battle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EffectUtils {

    public static Bitmap[] loadFrames(Context context, AttackType type) {
        Resources res = context.getResources();
        String prefix;
        int frameCount;

        switch (type) {
            case MELEE:
                prefix = "attack_effect_melee_";
                frameCount = 4;
                break;
            case POWER:
                prefix = "attack_effect_power_";
                frameCount = 5;
                break;
            case LONG_RANGE:
                prefix = "attack_effect_range_";
                frameCount = 5;
                break;
            default:
                throw new IllegalArgumentException("Unsupported AttackType: " + type);
        }

        Bitmap[] frames = new Bitmap[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String resourceName = prefix + (i + 1);
            int resId = res.getIdentifier(resourceName, "drawable", context.getPackageName());
            if (resId == 0) {
                throw new RuntimeException("Missing resource: " + resourceName);
            }
            frames[i] = BitmapFactory.decodeResource(res, resId);
        }

        return frames;
    }
}

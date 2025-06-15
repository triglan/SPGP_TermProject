package com.example.deadlockduel.framework.battle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.deadlockduel.scene.MainScene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectManager {
    private static final EffectManager instance = new EffectManager();
    public static EffectManager getInstance() { return instance; }

    private final List<AttackEffect> effects = new ArrayList<>();
    private final Paint paint = new Paint();

    private MainScene mainScene; // ✅ MainScene 참조

    private EffectManager() {}

    // ✅ MainScene 등록
    public void setScene(MainScene scene) {
        this.mainScene = scene;
    }

    // ✅ 사운드 재생을 포함한 효과 재생
    public void playEffect(AttackType type, float x, float y, boolean faceRight) {
        Bitmap[] frames = type.effectFrames;
        float scale = type.effectScale;        // ✅ 반영
        AttackEffect effect = new AttackEffect(frames, (int)x, (int)y, faceRight, scale); // ✅ 반영
        effects.add(effect);

        if (mainScene != null) {
            mainScene.playEffectSound(type);
        }
    }


    public void update() {
        Iterator<AttackEffect> iter = effects.iterator();
        while (iter.hasNext()) {
            AttackEffect effect = iter.next();
            effect.update();
            if (effect.isDone()) iter.remove();
        }
    }

    public void draw(Canvas canvas) {
        for (AttackEffect effect : effects) {
            effect.draw(canvas, paint);
        }
    }

    public List<AttackEffect> getEffects() {
        return effects;
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public void clear() {
        effects.clear();
    }
}

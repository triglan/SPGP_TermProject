package com.example.deadlockduel.framework.battle;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectManager {
    private final List<AttackEffect> effects = new ArrayList<>();
    private final Paint paint = new Paint();

    public void addEffect(AttackEffect effect) {
        effects.add(effect);
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

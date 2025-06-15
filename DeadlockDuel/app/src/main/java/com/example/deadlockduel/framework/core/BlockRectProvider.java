package com.example.deadlockduel.framework.core;

import android.graphics.Rect;

// 위치: 예) com.example.deadlockduel.framework.core
public interface BlockRectProvider {
    Rect getBlockRect(int blockIndex);
}

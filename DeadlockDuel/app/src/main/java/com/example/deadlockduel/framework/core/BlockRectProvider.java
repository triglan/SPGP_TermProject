package com.example.deadlockduel.framework.core;

import android.graphics.Rect;

import com.example.deadlockduel.object.Block;

// 위치: 예) com.example.deadlockduel.framework.core
public interface BlockRectProvider {
    Rect getBlockRect(int index);
    Block[] getAllBlocks(); // 🔥 블럭 전체 접근 추가
}

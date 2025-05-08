package com.example.deadlockduel.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.Scene;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Player;

public class MainScene implements Scene {
    private final Bitmap background;
    private final Paint blockPaint = new Paint();
    private final Block[] blocks;
    private final Player player;

    public MainScene(Resources res, int screenWidth, int screenHeight) {
        background = BitmapFactory.decodeResource(res, R.drawable.map1);

        int blockCount = 5;
        int gridCount = 13;
        blocks = new Block[blockCount];

        int[] blockColors = {
                Color.RED, Color.GREEN, Color.BLACK, Color.RED, Color.GREEN
        };

        int gridWidth = screenWidth / gridCount;
        int blockWidth = gridWidth;
        int blockHeight = blockWidth / 5;
        int top = (int)(screenHeight * 4f / 5f - blockHeight / 2);
        int startIndex = (gridCount - blockCount) / 2;

        for (int i = 0; i < blockCount; i++) {
            int gridIndex = startIndex + i;
            int left = gridIndex * gridWidth;
            Rect rect = new Rect(left, top, left + blockWidth, top + blockHeight);
            blocks[i] = new Block(rect, blockColors[i]);
        }

        player = new Player(res); // 🔥 스프라이트 확대는 여기서
        player.setBlockIndex(0);
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(background, null, dst, null);

        for (Block block : blocks) {
            block.draw(canvas, blockPaint);
        }

        Rect playerBlockRect = blocks[player.getBlockIndex()].getRect();
        player.updatePositionFromBlock(playerBlockRect);
        player.draw(canvas);
    }

    @Override public boolean onTouchEvent(MotionEvent event) { return false; }
    @Override public void onEnter() { }
    @Override public void onExit() { }
    @Override public void onPause() { }
    @Override public void onResume() { }
    @Override public boolean onBackPressed() { return false; }
}

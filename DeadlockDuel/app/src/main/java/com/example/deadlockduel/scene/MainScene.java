package com.example.deadlockduel.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.deadlockduel.R;
import com.example.deadlockduel.framework.Scene;
import com.example.deadlockduel.object.Block;
import com.example.deadlockduel.object.Player;
import com.example.deadlockduel.framework.StageConfig;

public class MainScene implements Scene {
    private Bitmap background;
    private Block[] blocks;
    private Player player;
    private Paint blockPaint = new Paint();

    private int blockCount; // ✅ 변경: 지역변수 → 멤버변수로 승격

    public Player getPlayer() {
        return this.player;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public MainScene(Resources res, int screenWidth, int screenHeight, StageConfig config) {
        background = BitmapFactory.decodeResource(res, config.backgroundResId);

        this.blockCount = config.blockCount;
        int gridCount = 13;
        blocks = new Block[blockCount];

        int gridWidth = screenWidth / gridCount;
        int blockWidth = gridWidth;
        int blockHeight = blockWidth / 5;
        int top = (int)(screenHeight * 4f / 5f - blockHeight / 2);
        int startIndex = (gridCount - blockCount) / 2;

        for (int i = 0; i < blockCount; i++) {
            int gridIndex = startIndex + i;
            int left = gridIndex * gridWidth;
            Rect rect = new Rect(left, top, left + blockWidth, top + blockHeight);
            blocks[i] = new Block(rect);
        }

        player = new Player(res);
        player.setBlockCount(blockCount);
        player.reset(config.playerStartIndex, config.playerFaceRight);
    }

    @Override
    public void update() {
        for (Block block : blocks) {
            block.reset();
        }

        player.update();
        player.updateBlockState(blocks);
        // blocks[enemyIndex].setHasEnemy(true);
        // blocks[enemyAttackIndex].setWillBeAttacked(true);
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

package com.example.deadlockduel.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TileMapView extends View {

    private final int tileSize = 150;
    private final int tileCount = 5;
    private final int tileMargin = 20;
    private final Paint paint = new Paint();

    public TileMapView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. 배경색
        canvas.drawColor(Color.rgb(80, 100, 80)); // 대체용 배경

        // 2. 바닥 타일 5개
        int y = getHeight() - tileSize - 100; // 바닥 위치
        for (int i = 0; i < tileCount; i++) {
            int x = 100 + i * (tileSize + tileMargin);
            paint.setColor(Color.DKGRAY);
            canvas.drawRect(x, y, x + tileSize, y + tileSize, paint);
        }

        // 3. 플레이어 (가운데 위치)
        paint.setColor(Color.BLUE);
        int playerX = 100 + 2 * (tileSize + tileMargin);
        canvas.drawRect(playerX, y - tileSize, playerX + tileSize, y, paint);
    }
}
package com.example.deadlockduel.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;

public class TileMapView extends View {
    private final int tileSize = 150;
    private final int tileMargin = 20;
    private final int bottomPadding = 100;

    private int tileCount = 5; // 언제든지 변경 가능
    private final ArrayList<Point> tileCenters = new ArrayList<>();
    private final Paint paint = new Paint();
    private final Player player;

    public TileMapView(Context context) {
        super(context);
        player = new Player(context);
        player.setLocation(1); // 초기위치 (인덱스)
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(80, 100, 80));

        tileCenters.clear();
        int totalWidth = tileCount * tileSize + (tileCount - 1) * tileMargin;
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - tileSize - bottomPadding;

        // 타일 중앙 좌표 저장 + 그리기
        for (int i = 0; i < tileCount; i++) {
            int left = startX + i * (tileSize + tileMargin);
            int centerX = left + tileSize / 2;
            tileCenters.add(new Point(centerX, y + tileSize / 2)); // 타일 중심 저장

            paint.setColor(Color.DKGRAY);
            canvas.drawRect(left, y, left + tileSize, y + tileSize, paint);
        }

        // 캐릭터 그리기 (중심 위치 기준)
        int index = player.getLocation();
        if (index >= 0 && index < tileCenters.size()) {
            Point center = tileCenters.get(index);
            player.setDrawCenter(center.x, center.y - tileSize / 2); // 블록 위에
        }

        player.update();
        player.draw(canvas);
        invalidate(); // 계속 그리기 위해 필요
    }
}

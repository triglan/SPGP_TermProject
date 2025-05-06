package com.example.deadlockduel.game;

import android.content.Context;
import android.graphics.Canvas;
import com.example.deadlockduel.R;

public class Player {
    private AnimSprite idleSprite;
    private int playerLocation = 0;
    private int drawX, drawY;
    private int scale = 1                                                       ;
    public Player(Context context) {
        idleSprite = new AnimSprite(context, R.drawable.player_samurai,
                4, 100, 125, 0, 0, scale);
        //idleSprite = new AnimSprite(context, R.drawable.player, 4, 256, 256, 2, 0);
    }

    public void setLocation(int locationIndex) {
        this.playerLocation = locationIndex;
    }

    public int getLocation() {
        return playerLocation;
    }

    public void setDrawCenter(int centerX, int bottomY) {
        this.drawX = centerX - (idleSprite.getFrameWidth() * scale) / 2;
        this.drawY = bottomY - (idleSprite.getFrameHeight() * scale);
    }

    public void update() {
        idleSprite.update();
    }

    public void draw(Canvas canvas) {
        idleSprite.draw(canvas, drawX, drawY);
    }
}

package com.example.deadlockduel.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.game.TileMapView;

public class DeadlockDuel extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다음 단계에서 만들 TileMapView 를 붙일 거야
        TileMapView mapView = new TileMapView(this);
        setContentView(mapView);
    }
}
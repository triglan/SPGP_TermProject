package com.example.deadlockduel.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deadlockduel.game.TileMapView;

public class DeadlockDuel extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TileMapView mapView = new TileMapView(this);
        setContentView(mapView);
    }
}
package com.walerider.nnmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("5b6aeb4b-f09d-4162-8f64-9068e24cbced");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

    }
}
package com.walerider.nnmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    protected FragmentContainerView fragmentContainerView;
    private NavHostFragment navHostFragment;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("5b6aeb4b-f09d-4162-8f64-9068e24cbced");
        setContentView(R.layout.activity_main);
        db = getBaseContext().openOrCreateDatabase("app.db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(login TEXT,email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS positions(name TEXT,coordinates TEXT PRIMARY KEY)");
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    protected void favoritesFileOpen(){
        try {
            InputStream inputStream = this.openFileInput("favorites.txt");
            Log.e("firstStart","not a first start");
            Log.e("firstStart", this.getPackageResourcePath());
            inputStream.close();
        }catch (FileNotFoundException e){
            Log.e("firstStart", "file Not Found" + e);
            writeToFile("feg",this);
        } catch (IOException e) {
            Log.e("firstStart", "Can not read file: " + e);
        }
    }
    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("start.txt", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
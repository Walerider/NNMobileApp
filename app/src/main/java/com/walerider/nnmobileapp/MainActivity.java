package com.walerider.nnmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.database.Cursor;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    private boolean isLogin = false;
    protected FragmentContainerView fragmentContainerView;
    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    private NavHostFragment navHostFragment;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("5b6aeb4b-f09d-4162-8f64-9068e24cbced");
        setContentView(R.layout.activity_main);
        db = getBaseContext().openOrCreateDatabase("app.db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(login TEXT PRIMARY KEY,email TEXT, password TEXT,tags TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS positions(title TEXT PRIMARY KEY,coordinates TEXT,address TEXT,photo INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS users_favorites(user_login TEXT, favorite_title TEXT, " +
                "FOREIGN KEY (user_login) REFERENCES users(login), FOREIGN KEY (favorite_title) REFERENCES positions(title))");
        /*db.execSQL("CREATE TABLE IF NOT EXISTS feedbacks(title TEXT,user_login TEXT,valuation INTEGER," +
                "FOREIGN KEY(title) REFERENCES positions(title), FOREIGN KEY (user_login) REFERENCES users(login))");*/
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = navHostFragment.getNavController();
        Log.e("1", String.valueOf(NavigationUI.onNavDestinationSelected(bottomNavigationView.getMenu().getItem(0), navController)));
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    protected void favoritesFileOpen(){
        try {
            InputStream inputStream = this.openFileInput("favorites.txt");
            Log.e("favorites","favorites");

            inputStream.close();
        }catch (FileNotFoundException e){
            Log.e("firstStart", "file Not Found" + e);
            writeToFile("feg",this,"favorites");
        } catch (IOException e) {
            Log.e("favorites", "Can not read file: " + e);
        }
    }
    private void writeToFile(String data, Context context,String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName +".txt", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public List<FavoriteItem> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(List<FavoriteItem> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }
}
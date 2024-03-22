package com.walerider.nnmobileapp;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.Manifest;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    private boolean isLogin = false;
    private boolean isServiceOnline = false;
    private String login;
    protected FragmentContainerView fragmentContainerView;
    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    private NavHostFragment navHostFragment;
    private SQLiteDatabase db;
    private LocationManager locationManager;
    private LocationListener locationListener;
    BottomNavigationView bottomNavigationView;
    private double longtitude;
    private double lantitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("5b6aeb4b-f09d-4162-8f64-9068e24cbced");
        setContentView(R.layout.activity_main);
        db = this.openOrCreateDatabase("app.db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(login TEXT PRIMARY KEY,fio TEXT, password TEXT,UNIQUE(login))");
        db.execSQL("CREATE TABLE IF NOT EXISTS positions(title TEXT PRIMARY KEY,coordinates TEXT,address TEXT,photo INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS users_favorites(user_login TEXT, favorite_title TEXT, " +
                "FOREIGN KEY (user_login) REFERENCES users(login), FOREIGN KEY (favorite_title) REFERENCES positions(title))");
        Cursor q1 = db.rawQuery("SELECT * FROM users_favorites WHERE user_login='Walerider'",null);
        Cursor q2 = db.rawQuery("SELECT * FROM users WHERE login='Walerider'",null);
        if(!q2.moveToFirst()){db.execSQL("INSERT OR IGNORE INTO users VALUES('Walerider','Нелюбин Ярослав Садаевич','Password');");}
        if(!q1.moveToFirst()){db.execSQL("INSERT OR IGNORE INTO users_favorites VALUES ('Walerider','Собор Александра Невского'), ('Walerider','Ижевский государственный технический университет имени М. Т. Калашникова');");}
        db.execSQL("INSERT OR IGNORE INTO positions VALUES('Собор Александра Невского','56.844068,53.200970','улица Максима Горького, 66',"
        + Integer.toString(R.drawable.sobor) + ");");
        db.execSQL("INSERT OR IGNORE INTO positions VALUES('Кафедральный собор Архангела Михаила','56.849569,53.205296','улица Карла Маркса, 222'," +
                Integer.toString(R.drawable.caf_sobor_archangela) + ");");
        db.execSQL("INSERT OR IGNORE INTO positions VALUES('Государственный зоологический парк Удмуртии','56.865361,53.174488','улица Кирова, 8'," +
                Integer.toString(R.drawable.zoo) + ");");
        db.execSQL("INSERT OR IGNORE INTO positions VALUES('Ижевский государственный технический университет имени М. Т. Калашникова','56.871179,53.174995'," +
                "'Студенческая улица, 7'," + Integer.toString(R.drawable.istu) + ");");
        /*db.execSQL("CREATE TABLE IF NOT EXISTS feedbacks(title TEXT,user_login TEXT,valuation INTEGER," +
                "FOREIGN KEY(title) REFERENCES positions(title), FOREIGN KEY (user_login) REFERENCES users(login))");*/
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        checkLogin();
        NavController navController = navHostFragment.getNavController();
        Log.e("1", String.valueOf(NavigationUI.onNavDestinationSelected(bottomNavigationView.getMenu().getItem(0), navController)));
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    protected void checkLogin(){
        try {
            InputStream inputStream = this.openFileInput("login.txt");
            Log.e("login","login");
            setLogin(true);
            settingUpLogin();
            inputStream.close();
        }catch (FileNotFoundException e){
            setLogin(false);
        } catch (IOException e) {
            Log.e("login", "Can not read file: " + e);
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
    protected void settingUpLogin(){
        if(!isLogin)return;
        String fullName = "";
        InputStream fin = null;
        try {
            fin = this.openFileInput("login.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF8"));
            String name = "";
            String i;
            while((i=in.readLine()) != null){
                name += i;
            }
            fullName = name;
            in.close();
        }catch (FileNotFoundException e){
            setLogin(false);
        }
        catch (IOException e) {
            Log.e("login123123", "Can not read file: " + e);
        }
        finally {
            try {
                if(fin != null)
                    fin.close();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        Cursor q = db.rawQuery("SELECT * FROM users WHERE fio = '" + fullName + "';",null);
        if(q.moveToFirst()){
            setLogin(q.getString(0));
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public List<FavoriteItem> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(List<FavoriteItem> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}

package com.walerider.nnmobileapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkCreatedCallback;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;


public class HomeFragment extends Fragment {
    private double longtitude;
    private double latitude;
    private double posLatitude;
    private double posLongtitude;
    private boolean isServiceOnline = false;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView;
    TextView longtitudeTextView;
    private SQLiteDatabase db;
    MapView mapView;

    public HomeFragment() {
    }

    // TODO: 14.03.2024 сделать точки 
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = (MainActivity)getActivity();
        db = activity.getDb();
        mapView = v.findViewById(R.id.mapview);/*
        latitudeTextView = v.findViewById(R.id.lantitude);
        longtitudeTextView = v.findViewById(R.id.longtitude);*/
        if(getArguments() != null){
            posLatitude = getArguments().getDouble("latitudePos");
            posLongtitude = getArguments().getDouble("longitudePos");
        }
        Cursor query = db.rawQuery("SELECT * FROM positions",null);
        while (query.moveToNext()){
            String[] position = query.getString(1).split(",");
            double posLatitude = Double.parseDouble(position[0]);
            double posLongtitude = Double.parseDouble(position[1]);
            mapView.getMapWindow().getMap().getMapObjects().addPlacemark(new PlacemarkCreatedCallback() {
                @Override
                public void onPlacemarkCreated(@NonNull PlacemarkMapObject placemarkMapObject) {
                    placemarkMapObject.setIcon(ImageProvider.fromResource(activity.getApplicationContext(),R.drawable.pin));
                    IconStyle i = new IconStyle();
                    i.setScale(0.05f);
                    placemarkMapObject.setIconStyle(i);
                    placemarkMapObject.setGeometry(new Point(posLatitude,posLongtitude));
                    placemarkMapObject.setUserData(query.getString(0) + "_" + query.getString(2)+ "_" +query.getInt(3));
                }
            }).addTapListener(placemarkTapListener);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLastLocation();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private Object getSystemService(String locationService) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();/*
                            latitudeTextView.setText(latitude + "");
                            longtitudeTextView.setText(longtitude + "");*/
                            if(posLatitude != 0){
                                mapView.getMapWindow().getMap().move(new CameraPosition(new Point(posLatitude,posLongtitude),17f,0,0));
                            }else{
                                mapView.getMapWindow().getMap().move(new CameraPosition(new Point(latitude,longtitude),17f,0,0));
                            }
                            mapView.getMapWindow().getMap().getMapObjects().addPlacemark(new PlacemarkCreatedCallback() {
                                @Override
                                public void onPlacemarkCreated(@NonNull PlacemarkMapObject placemarkMapObject) {
                                    placemarkMapObject.setIcon(ImageProvider.fromResource(getContext(),R.drawable.user_on_map));
                                    IconStyle i = new IconStyle();
                                    i.setScale(0.05f);
                                    placemarkMapObject.setIconStyle(i);
                                    placemarkMapObject.setGeometry(new Point(latitude,longtitude));
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longtitude = mLastLocation.getLongitude();
        }
    };
    private MapObjectTapListener placemarkTapListener = ((mapObject, point) -> {
        PositionFragment fragment = new PositionFragment(mapObject.getUserData().toString());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView,fragment,"forFind")
                .addToBackStack("fragmentContainerView")
                .commit();
        return true;
    });
}
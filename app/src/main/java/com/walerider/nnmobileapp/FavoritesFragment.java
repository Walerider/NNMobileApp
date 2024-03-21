package com.walerider.nnmobileapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesFragment extends Fragment {


    private SQLiteDatabase db;
    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView favoritesInfo;
    public FavoritesFragment() {
    }


    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        MainActivity activity = (MainActivity) getActivity();
        favoritesInfo = view.findViewById(R.id.favoritesInfo);
        if(activity.isLogin()){
        FavoritesAdapter.FavoritesItemClickListener favoritesItemClickListener = new FavoritesAdapter.FavoritesItemClickListener() {
            @Override
            public void onItemClick(FavoriteItem favoriteItem, int position) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                NavController navController = navHostFragment.getNavController();
                Bundle bundle = new Bundle();
                Log.d("COORD",favoriteItem.getCoordinates().split(",")[0]);
                Log.d("COORD",favoriteItem.getCoordinates().split(",")[1]);
                bundle.putDouble("latitudePos", Double.parseDouble(favoriteItem.getCoordinates().split(",")[0]));
                bundle.putDouble("longitudePos", Double.parseDouble(favoriteItem.getCoordinates().split(",")[1]));
                navController.navigate(R.id.homeFragment,bundle);
            }
        };
        db = activity.getDb();
        Cursor query = db.rawQuery("SELECT * FROM users_favorites WHERE user_login ='" + activity.getLogin() + "'",null);
        FavoritesAdapter adapter = new FavoritesAdapter(getActivity(),favoriteItems,favoritesItemClickListener);
        recyclerView = view.findViewById(R.id.RecView);
        recyclerView.setAdapter(adapter);
        while (query.moveToNext()){
            Cursor queryInWhile = db.rawQuery("SELECT * FROM positions WHERE title='" + query.getString(1) + "'",null);
            queryInWhile.moveToNext();
            favoriteItems.add(new FavoriteItem(queryInWhile.getInt(3),queryInWhile.getString(1),queryInWhile.getString(0),queryInWhile.getString(2)));
        }
        if(favoriteItems.size() == 0){
            favoritesInfo.setText("У вас нету избранных мест. Пора поискать!");
        }

        }
        else {
            favoritesInfo.setText("Вы ещё не вошли в аккаунт");
        }

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onStop() {
        favoriteItems.clear();
        super.onStop();
    }
}
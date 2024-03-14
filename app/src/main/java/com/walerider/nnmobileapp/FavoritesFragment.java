package com.walerider.nnmobileapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesFragment extends Fragment {


    private SQLiteDatabase db;
    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    private RecyclerView recyclerView;
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
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        FavoritesAdapter.FavoritesItemClickListener favoritesItemClickListener = new FavoritesAdapter.FavoritesItemClickListener() {
            @Override
            public void onItemClick(FavoriteItem favoriteItem, int position) {
                Toast.makeText(getActivity().getApplicationContext(),"Был выбран " + favoriteItem.getTitle(),Toast.LENGTH_LONG).show();
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.homeFragment);
            }
        };
        FavoritesAdapter adapter = new FavoritesAdapter(getActivity(),favoriteItems,favoritesItemClickListener);
        recyclerView = v.findViewById(R.id.RecView);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        favoriteItems.add(new FavoriteItem(R.drawable.sobor,"Собор Александа невского","ул. Максима Горького, 66"));
        favoriteItems.add(new FavoriteItem(R.drawable.sobor,"Собор Александа невского","ул. Максима Горького, 66"));
        favoriteItems.add(new FavoriteItem(R.drawable.sobor,"Собор Александа невского","ул. Максима Горького, 66"));
        super.onStart();
    }

    @Override
    public void onStop() {
        favoriteItems.clear();
        super.onStop();
    }
}
package com.walerider.nnmobileapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PositionFragment extends Fragment {
    private SQLiteDatabase db;
    String data;
    ImageView imageView;
    TextView titleTextView;
    TextView addressTextView;
    Button addToFavoritesButton;
    public PositionFragment() {
        // Required empty public constructor
    }

    public PositionFragment(String data) {
        this.data = data;
    }

    public static PositionFragment newInstance(String param1, String param2) {
        PositionFragment fragment = new PositionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        db = activity.getDb();
        String[] dataSplit = data.split("_");

        View view = inflater.inflate(R.layout.fragment_position, container, false);
        imageView = view.findViewById(R.id.imageViewPosition);
        imageView.setImageResource(Integer.parseInt(dataSplit[2]));
        titleTextView = view.findViewById(R.id.titleTextPosition);
        titleTextView.setText(dataSplit[0]);
        addressTextView = view.findViewById(R.id.addressTextPosition);
        addressTextView.setText(dataSplit[1]);
        addToFavoritesButton = view.findViewById(R.id.addToFavorites);
        addToFavoritesButton.setOnClickListener(v -> {
            if(!activity.isLogin()){
                Toast.makeText(getContext(),"Войдите в аккаунт, чтобы добавлять избранное",Toast.LENGTH_LONG).show();
            }else{
                // TODO: 21.03.2024 вот здесь поправить избранное
                Cursor checkQuery = db.rawQuery("SELECT * FROM users_favorites WHERE user_login = '" + activity.getLogin() + "' AND favorite_title = '"
                + dataSplit[0] + "';",null);
                if(!checkQuery.moveToFirst()){
                    db.execSQL("INSERT OR IGNORE INTO users_favorites VALUES('" + activity.getLogin() +"','" + dataSplit[0] + "');");
                }
            }
        });
        return view;
    }
}
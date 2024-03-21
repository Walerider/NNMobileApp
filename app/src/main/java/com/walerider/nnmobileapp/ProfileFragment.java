package com.walerider.nnmobileapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.Manifest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;


public class ProfileFragment extends Fragment {

    private NavController navController;
    private NavHostFragment navHostFragment;

    public ProfileFragment() {
    }
    private TextView profileName;
    private Button userFeedbacks;
    private Button exitFromAccount;
    
    private MainActivity activity;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        if(!activity.isLogin()){
            navController.navigate(R.id.registrationFragment);
        }
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profile_name);
        userFeedbacks = view.findViewById(R.id.feedbacks);
        exitFromAccount = view.findViewById(R.id.exit);
        getFullName();
        exitFromAccount.setOnClickListener(v -> {
            accountExit();
            navController.navigate(R.id.profileFragment);
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!activity.isLogin()){
            navController.navigate(R.id.registrationFragment);
        }
    }
    private void getFullName(){
        InputStream fin = null;
        try {
            fin = getActivity().openFileInput("login.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF8"));
            String name = "";
            String i;
            while((i=in.readLine()) != null){
                name += i;
            }
            profileName.setText(name);
            in.close();
        }catch (FileNotFoundException e){
            activity.setLogin(false);
        }
        catch (IOException e) {
            Log.e("login123123", "Can not read file: " + e);
        }
        finally {
                try {
                    if(fin != null)
                        fin.close();
                } catch (IOException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void accountExit(){
        getActivity().deleteFile("login.txt");
        activity.setLogin(false);
    }
}
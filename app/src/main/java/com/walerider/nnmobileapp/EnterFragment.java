package com.walerider.nnmobileapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.text.SpannableStringBuilder;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.text.Spannable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;


public class EnterFragment extends Fragment {


    private Button loginRedirect;
    private SQLiteDatabase db;
    private Button loginButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    private MainActivity activity;
    private NavController navController;
    private NavHostFragment navHostFragment;
    public EnterFragment() {
    }

    public static EnterFragment newInstance(String param1, String param2) {
        EnterFragment fragment = new EnterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter, container, false);
        loginRedirect = view.findViewById(R.id.loginRedirect);
        activity = (MainActivity) getActivity();
        db = activity.getDb();
        loginEditText = view.findViewById(R.id.LoginEditText);
        passwordEditText = view.findViewById(R.id.PasswordEditText);
        loginButton = view.findViewById(R.id.EnterButton);
        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        loginRedirect.setOnClickListener(v -> {
            navController = navHostFragment.getNavController();
            navController.navigate(R.id.registrationFragment);
        });
        loginButton.setOnClickListener(v ->{
            boolean checked = checkAllFields();
            if(checked){
                Cursor query =
                        db.rawQuery("SELECT * FROM users where login = '" + loginEditText.getText() + "'",null);
                if(!query.moveToFirst()){
                    loginEditText.setError("Пользователя с таким логином не существует");
                    return;
                }else{
                    if(!query.getString(2).equals(String.valueOf(passwordEditText.getText()))){
                        passwordEditText.setError("Введён неправильный пароль");
                        return;
                    }
                    writeToFile(query.getString(1),getContext(),"login");
                    activity.setLogin(query.getString(0));
                    activity.setLogin(true);
                    navController = navHostFragment.getNavController();
                    navController.navigate(R.id.profileFragment);
                }
            }
        });
        return view;
    }
    private boolean checkAllFields(){
        if(loginEditText.getText().length() == 0 && passwordEditText.getText().length() == 0){
            loginEditText.setError("Введите логин");
            passwordEditText.setError("Введите пароль");
            return false;
        }else if(loginEditText.getText().length() == 0){
            loginEditText.setError("Введите логин");
            return false;
        }else if(passwordEditText.getText().length() == 0){
            passwordEditText.setError("Введите пароль");
            return false;
        }

        return true;
    }
    protected void checkLogin(){
        try {
            InputStream inputStream = getActivity().openFileInput("login.txt");
            Log.e("login","login");
            activity.setLogin(true);
            inputStream.close();
        }catch (FileNotFoundException e){
            activity.setLogin(false);
        } catch (IOException e) {
            Log.e("login", "Can not read file: " + e);
        }
    }
    private void writeToFile(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName +".txt",getContext().MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
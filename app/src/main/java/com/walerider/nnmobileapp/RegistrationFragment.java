package com.walerider.nnmobileapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;


public class RegistrationFragment extends Fragment {

    private Button redirectButtonInRegister;
    private Button regButton;
    NavHostFragment navHostFragment;
    NavController navController;
    private EditText fullNameEditText;
    private EditText regLoginEditText;
    private EditText regPasswordEditText;
    private SQLiteDatabase db;
    private EditText regRepeatPasswordEditText;
    private MainActivity activity;
    private boolean isBeingOnline;

    public RegistrationFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // TODO: 20.03.2024 Забить хуй на нижнюю навигацию и доделать карту и збс
    // TODO: 20.03.2024 сделать добавление точки в фавориты(хз как)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        regButton = view.findViewById(R.id.RegButton);
        navHostFragment = (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        activity.getBottomNavigationView().setSelectedItemId(R.id.profileFragment);
        regRepeatPasswordEditText = view.findViewById(R.id.RepeatPasswordEditText);
        regPasswordEditText = view.findViewById(R.id.RegPasswordEditText);
        regLoginEditText = view.findViewById(R.id.RegLoginEditText);
        fullNameEditText = view.findViewById(R.id.FullNameEditText);
        redirectButtonInRegister = view.findViewById(R.id.registerRedirect);
        regButton.setOnClickListener(v -> {
            boolean allChecked = checkRegisterFields();
            if(allChecked){
                db = activity.getDb();
                Cursor query =
                        db.rawQuery("SELECT * FROM users where login = '" + regLoginEditText.getText() + "'",null);
                if(!query.moveToFirst()){
                    writeToFile(String.valueOf(fullNameEditText.getText()),getContext(),"login");
                    checkLogin();
                    activity.setLogin(query.getString(0));
                    navController.navigate(R.id.profileFragment);
                }else{
                    regLoginEditText.setError("Пользователь с таким логином уже существует");
                }
            }
        });
        redirectButtonInRegister.setOnClickListener(v ->{
            navController.navigate(R.id.enterFragment);
        });
        isBeingOnline = true;
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean checkRegisterFields(){
        if(fullNameEditText.length() == 0 && regLoginEditText.length() == 0
                && regPasswordEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            regLoginEditText.setError("Введите логин");
            regPasswordEditText.setError("Введите пароль");
            return false;
        } else if(fullNameEditText.length() == 0 && regLoginEditText.length() == 0
                && regRepeatPasswordEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            regLoginEditText.setError("Введите логин");
            regRepeatPasswordEditText.setError("Введите пароль ещё раз");
            return false;
        } else if(fullNameEditText.length() == 0 && regLoginEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            regLoginEditText.setError("Введите логин");
            return false;
        } else if(fullNameEditText.length() == 0 && regPasswordEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            regPasswordEditText.setError("Введите пароль");
            return false;
        }else if(fullNameEditText.length() == 0 && regRepeatPasswordEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            regRepeatPasswordEditText.setError("Введите пароль ещё раз");
            return false;
        }else if(regLoginEditText.length() == 0 && regPasswordEditText.length() == 0){
            regLoginEditText.setError("Введите логин");
            regPasswordEditText.setError("Введите пароль");
            return false;
        }else if(regLoginEditText.length() == 0 && regRepeatPasswordEditText.length() == 0){
            regLoginEditText.setError("Введите логин");
            regRepeatPasswordEditText.setError("Введите пароль ещё раз");
            return false;
        }else if(regPasswordEditText.length() == 0 && regRepeatPasswordEditText.length() == 0){
            regPasswordEditText.setError("Введите пароль");
            regRepeatPasswordEditText.setError("Введите пароль ещё раз");
            return false;
        }else if(regLoginEditText.length() == 0){
            regLoginEditText.setError("Введите логин");
            return false;
        }else if(fullNameEditText.length() == 0){
            fullNameEditText.setError("Введите ваше ФИО");
            return false;
        }else if(regPasswordEditText.length() == 0){
            regPasswordEditText.setError("Введите пароль");
            return false;
        } else if (regRepeatPasswordEditText.length() == 0) {
            regRepeatPasswordEditText.setError("Введите пароль ещё раз");
            return false;
        }else if(regPasswordEditText.length() < 8){
            regPasswordEditText.setError("Введите пароль больше 8 символов");
        }else if(!regPasswordEditText.getText().toString().equals(regRepeatPasswordEditText.getText().toString())){
            regPasswordEditText.setError("Пароль и повтор пароля разные");
            regRepeatPasswordEditText.setError("Пароль и повтор пароля разные");
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
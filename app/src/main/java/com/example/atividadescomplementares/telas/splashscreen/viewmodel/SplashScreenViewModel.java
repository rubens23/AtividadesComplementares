package com.example.atividadescomplementares.telas.splashscreen.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.login.LoginFirebase;

public class SplashScreenViewModel extends AndroidViewModel {


    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean checarSeJaTemUserLogado(){
        Log.d("monitoringContext", "contexto: "+getApplication());

        LoginFirebase loginFirebase = new LoginFirebase(getApplication());
        return loginFirebase.checarSeTemUserLogado();
    }


}

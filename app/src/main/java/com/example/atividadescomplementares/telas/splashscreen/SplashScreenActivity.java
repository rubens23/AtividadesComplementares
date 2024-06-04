package com.example.atividadescomplementares.telas.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.example.atividadescomplementares.MainActivity;
import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.telas.splashscreen.viewmodel.SplashScreenViewModel;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        mViewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);


    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean temUserLogado = mViewModel.checarSeJaTemUserLogado();
        startNewActivityWithBundle(temUserLogado);

    }

    public void startNewActivityWithBundle(boolean isLoggedIn){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("isLoggedIn", isLoggedIn);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}


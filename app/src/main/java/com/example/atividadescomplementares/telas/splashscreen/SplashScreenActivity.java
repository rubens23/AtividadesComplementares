package com.example.atividadescomplementares.telas.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import androidx.core.splashscreen.SplashScreen;
import com.example.atividadescomplementares.MainActivity;
import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.telas.splashscreen.viewmodel.SplashScreenViewModel;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //splashScreen.setKeepOnScreenCondition(()->true);





//        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//
//        changeStatusBarTextColor();
//        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//
//        if (currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.mainPink));
//
//        }else {
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//
//        }






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


    private void changeStatusBarTextColor() {
        //muda a cor da status bar de acordo com modo escuro ou claro
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            }
        }
    }
}


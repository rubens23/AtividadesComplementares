package com.example.atividadescomplementares;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.example.atividadescomplementares.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    //instancia do viewBinding
    private ActivityMainBinding binding;

    /*
    instancia do navHostFragment.

    O NavHostFragment é um container para fragments. Você pode colocar quantos fragments voce quiser
    dentro desse container. Esse container fica localizado no arquivo xml da main activity

    Esses fragments dentro do container podem ser controlados atraves de um grafico de navegação para
    consegir fazer a navegação entre as telas do app
     */
    private NavHostFragment navHostFragment;

    /*
    o navController possibilita que controlemos o grafico de navegação para navegar entre as telas do nosso app
     */
    private NavController navController;


    //metodo que 'desenha' o arquivo xml na tela
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //configuracao do binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mudança da cor da barra de status(aquela barra onde fica as horas e os icones de notificaçao)
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        changeStatusBarTextColor();


        //configurar a aparencia da barra de navegação inferior
        configBottomNavigationAppearance();
        configNavigation();


    }


    private void configBottomNavigationAppearance() {
        binding.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.bottomNavigationColor));
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

    private void configNavigation() {
        //configura o navController para habilitar a navegação
        //é importante falar tambem que é feita uma vinculação da barra de navegação inferior e o navController para conseguir navegar usando
        //a barra inferior
        navHostFragment = (NavHostFragment) (getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId()));
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {


                    int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                    if (navDestination.getId() == R.id.fragmentBoasVindas ||
                            navDestination.getId() == R.id.fragmentCadastro
                            || navDestination.getId() == R.id.fragmentLogin
                            || navDestination.getId() == R.id.fragmentRecuperarSenha) {
                        binding.bottomNavigationView.setVisibility(View.GONE);
                    } else {
                        binding.bottomNavigationView.setVisibility(View.VISIBLE);


                    }

                    //condição para setar a cor da status bar de acordo com o fragment
                    if (navDestination.getId() == R.id.fragmentBoasVindas) {
                        if (currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.mainPink));

                        }


                    } else {

                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColor1));


                    }
                }
            });

        }


    }


}
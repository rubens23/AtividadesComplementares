package com.example.atividadescomplementares.dados.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * classe de acesso aos metodos de autenticação no firebase(login)
 */
public class LoginFirebase {



    private FirebaseAuth mAuth;
    private String tag = "LoginFirebase";

    private Context context;

    //SharedPreferences para a persistência do login
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isLoggedIn;
    private String userEmail;


    //inicialização do firebase auth para os metodos que o necessitam nessa classe
    public LoginFirebase(Context context){
        Log.d("monitoringContext", "contexto: "+context);
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        userEmail = sharedPreferences.getString("userEmail", "");

    }


    //função para fazer login no app
    public void fazerLogin(String email, String senha, LoginCallback loginCallback){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            persistirNovoLoginNoSharedPreferences(email);
                            loginCallback.onLogin(true);


                        }else {
                            loginCallback.onLogin(false);
                            Log.e(tag, "erro no metodo fazerLogin: "+task.getException());
                        }

                    }
                });
    }

    private void persistirNovoLoginNoSharedPreferences(String email) {
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", email);
        editor.apply();
    }


    //função para fazer logout no app
    private void fazerLogout(LogoutCallback logoutCallback){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
            desfazerPersistenciaDeLogin();
            logoutCallback.onLogout(true);

        }else {
            //o user nem tava logado
            logoutCallback.onLogout(false);
        }
    }

    private void desfazerPersistenciaDeLogin() {
        editor.clear();
        editor.apply();
    }

    public boolean checarSeTemUserLogado(){

        if(isLoggedIn && !userEmail.isEmpty()){
            //tem user no shared preferences
            return true;

        }else {
            return false;

        }

    }
}

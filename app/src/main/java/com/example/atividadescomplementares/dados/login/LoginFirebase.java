package com.example.atividadescomplementares.dados.login;

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


    //inicialização do firebase auth para os metodos que o necessitam nessa classe
    public LoginFirebase(){
        this.mAuth = FirebaseAuth.getInstance();

    }


    //função para fazer login no app
    public void fazerLogin(String email, String senha, LoginCallback loginCallback){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginCallback.onLogin(true);


                        }else {
                            loginCallback.onLogin(false);
                            Log.e(tag, "erro no metodo fazerLogin: "+task.getException());
                        }

                    }
                });
    }


    //função para fazer logout no app
    private void fazerLogout(LogoutCallback logoutCallback){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
            logoutCallback.onLogout(true);

        }else {
            //o user nem tava logado
            logoutCallback.onLogout(false);
        }
    }
}

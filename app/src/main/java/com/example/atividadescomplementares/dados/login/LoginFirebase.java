package com.example.atividadescomplementares.dados.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFirebase {
    private FirebaseAuth mAuth;
    private String tag = "LoginFirebase";

    public LoginFirebase(){
        this.mAuth = FirebaseAuth.getInstance();

    }

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

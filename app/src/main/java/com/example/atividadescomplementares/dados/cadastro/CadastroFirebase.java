package com.example.atividadescomplementares.dados.cadastro;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroFirebase {
    private FirebaseAuth mAuth;
    private String tag = "CadastroFirebase";

    public CadastroFirebase(){
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void cadastrarNovoUsuario(String email, String password, CadastroCallback cadastroCallback){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            cadastroCallback.onCadastrou(true);

                        }else {
                            cadastroCallback.onCadastrou(false);
                            Log.e(tag, "erro no metodo cadastrar novo usuario: "+task.getException());
                        }

                    }
                });
    }
}



package com.example.atividadescomplementares.dados.recuperarSenha;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * classe com metodo para recuperação de senha(mudança de senha)
 */
public class RecuperarSenhaFirebase {
    private FirebaseAuth mAuth;
    private String tag = "RecuperarSenhaFirebase";


    //inicialização do firebase auth
    public RecuperarSenhaFirebase(){
        this.mAuth = FirebaseAuth.getInstance();

    }


    //metodo para enviar email para mudança de senha
    public void mandarEmailParaResetarSenha(String email, PasswordResetCallback passwordResetCallback){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            passwordResetCallback.onEmailEnviado(true);
                        }else{
                            passwordResetCallback.onEmailEnviado(false);
                            Log.e(tag, "erro no metodo mandarEmailParaResetarSenha: "+task.getException());
                        }
                    }
                });

    }

}

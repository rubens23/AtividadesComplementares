package com.example.atividadescomplementares.telas.recuperarSenha.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.recuperarSenha.PasswordResetCallback;
import com.example.atividadescomplementares.dados.recuperarSenha.RecuperarSenhaFirebase;

public class FragmentRecuperarSenhaViewModel extends ViewModel {

    private RecuperarSenhaFirebase recuperarSenhaFirebase = new RecuperarSenhaFirebase();

    public MutableLiveData<String> resultadoEnvioEmailRecuperacao = new MutableLiveData<>();

    public void enviarEmailDeRecuperacaoDeSenha(String email) {
        recuperarSenhaFirebase.mandarEmailParaResetarSenha(email, new PasswordResetCallback() {
            @Override
            public void onEmailEnviado(boolean enviouEmail) {
                if(enviouEmail){
                    resultadoEnvioEmailRecuperacao.setValue("um email foi enviado para você recuperar seu email. Cheque seu e-mail cadastrado!");
                }else {
                    resultadoEnvioEmailRecuperacao.setValue("ocorreu uma falha no envio do email de recuperação. Tente novamente!");

                }
            }
        });
    }
}
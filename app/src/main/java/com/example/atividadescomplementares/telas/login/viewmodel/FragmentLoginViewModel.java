package com.example.atividadescomplementares.telas.login.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.login.LoginCallback;
import com.example.atividadescomplementares.dados.login.LoginFirebase;

public class FragmentLoginViewModel extends ViewModel {
    public boolean olhoDaSenhaAberto = false;

    private LoginFirebase loginFirebase = new LoginFirebase();

    public MutableLiveData<String> resultadoLogin = new MutableLiveData<>();

    public void fazerLogin(String email, String senha) {
        loginFirebase.fazerLogin(email, senha, new LoginCallback() {
            @Override
            public void onLogin(boolean sucesso) {
                if(sucesso){
                    resultadoLogin.setValue("login feito com sucesso");
                }else {
                    resultadoLogin.setValue("Erro no login, cheque a conexão e tente novamente!");

                }
            }
        });
    }
}
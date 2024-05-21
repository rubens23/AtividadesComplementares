package com.example.atividadescomplementares.telas.login.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.login.LoginCallback;
import com.example.atividadescomplementares.dados.login.LoginFirebase;


/**
 * view Model para acessar os dados e notificar o fragment quando esses dados forem obtidos/alterados
 */
public class FragmentLoginViewModel extends AndroidViewModel {

    //essa variavel controla a visibilidade do olho de mostrar senha
    public boolean olhoDaSenhaAberto = false;


    //instancia da classe de acesso ao firebase
    private LoginFirebase loginFirebase = new LoginFirebase(getApplication());


    //live data para fornecer um observer para o fragment(view)
    public MutableLiveData<String> resultadoLogin = new MutableLiveData<>();

    public FragmentLoginViewModel(@NonNull Application application) {
        super(application);
    }


    //metodo para fazer login
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
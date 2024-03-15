package com.example.atividadescomplementares.telas.recuperarSenha.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.recuperarSenha.PasswordResetCallback;
import com.example.atividadescomplementares.dados.recuperarSenha.RecuperarSenhaFirebase;


/**
 * viewModel da tela de recuperar senha(trocar senha)
 *
 *    ------------------->     -------->
 * tela(fragment) <---> viewModel<--->dados(classes do firebase)
 *       <----------------     <---------
 */
public class FragmentRecuperarSenhaViewModel extends ViewModel {


    //instancia da classe de acesso ao firebase
    private RecuperarSenhaFirebase recuperarSenhaFirebase = new RecuperarSenhaFirebase();


    //livedata do resultado do envio do email de troca de senha
    public MutableLiveData<String> resultadoEnvioEmailRecuperacao = new MutableLiveData<>();


    //metodo para enviar um email de troca de senha
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
package com.example.atividadescomplementares.telas.configuracoes.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.atividadeComplementar.SalvouNovaCargaHoraria;
import com.example.atividadescomplementares.dados.cadastro.MudouEmail;
import com.example.atividadescomplementares.dados.recuperarSenha.PasswordResetCallback;
import com.example.atividadescomplementares.dados.recuperarSenha.RecuperarSenhaFirebase;
import com.example.atividadescomplementares.dados.user.PegouCargaHorariaTotal;
import com.example.atividadescomplementares.dados.user.UserFirebase;

import java.util.Objects;

public class FragmentConfiguracoesViewModel extends ViewModel {

    public String emailInicial = "";
    public Integer cargaNecessariaInicial = 0;

    public MutableLiveData<Integer> cargaTotalNecessaria = new MutableLiveData<>();

    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();

    private UserFirebase userFirebase = new UserFirebase();

    private RecuperarSenhaFirebase recuperarSenhaFirebase = new RecuperarSenhaFirebase();

    public MutableLiveData<String> resultadoEnvioEmailRecuperacao = new MutableLiveData<>();

    public MutableLiveData<String> resultadoSaveNovoEmail = new MutableLiveData<>();

    public MutableLiveData<String> resultadoSaveNovaCargaHoraria = new MutableLiveData<>();

    public boolean podeMostrarSnackDeRecuperacaoDeSenha = false;

    public boolean podeMostrarSnackDeSaveDeNovoEmail = false;
    public boolean podeMostrarSnackDeSaveDeNovaCargaHoraria = false;




    public void pegarCargaTotal() {
        persistenciaFirebase.pegarCargaTotalNecessaria(new PegouCargaHorariaTotal() {
            @Override
            public void pegouCargaTotal(Integer cargaTotal) {
                cargaTotalNecessaria.setValue(cargaTotal);

            }
        });
    }

    public String pegarEmail() {
        return userFirebase.pegarEmailDoUser();
    }

    public void enviarEmailDeRecuperacaoDeSenha() {
        if(!Objects.equals(emailInicial, "")){
            recuperarSenhaFirebase.mandarEmailParaResetarSenha(emailInicial, new PasswordResetCallback() {
                @Override
                public void onEmailEnviado(boolean enviouEmail) {
                    if(enviouEmail){
                        podeMostrarSnackDeRecuperacaoDeSenha = true;
                        resultadoEnvioEmailRecuperacao.setValue("um email foi enviado para você recuperar seu email. Cheque seu e-mail cadastrado!");
                    }else {
                        podeMostrarSnackDeRecuperacaoDeSenha = true;
                        resultadoEnvioEmailRecuperacao.setValue("ocorreu uma falha no envio do email de recuperação. Tente novamente!");

                    }
                }
            });
        }

    }



    public void salvarNovoEmail(String email, String senha, String emailNovo) {
        persistenciaFirebase.mudarEmailDoUserNoFirebaseAuth(email, senha, emailNovo, new MudouEmail() {
            @Override
            public void mudouEmail(boolean mudou) {
                if(mudou){
                    salvarNovoEmailNoDb(emailNovo);
                    podeMostrarSnackDeSaveDeNovoEmail = true;
                    resultadoSaveNovoEmail.setValue("E-mail alterado com sucesso! Verifique seu e-mail para confirmar a alteração!");

                }else{
                    podeMostrarSnackDeSaveDeNovoEmail = true;

                    resultadoSaveNovoEmail.setValue("Houve um erro ao salvar o novo e-mail..Tente novamente!");
                }

            }
        });
    }

    private void salvarNovoEmailNoDb(String emailNovo) {
        persistenciaFirebase.alterarEmailNoRealtimeDb(emailNovo);
    }

    public void alterarCargaHorariaTotalDoUser(Integer novaCargaHoraria) {
        persistenciaFirebase.alterarCargaHorariaTotalDoUser(novaCargaHoraria, new SalvouNovaCargaHoraria() {
            @Override
            public void salvouNovaCargaHoraria(boolean salvou) {
                if(salvou){
                    podeMostrarSnackDeSaveDeNovaCargaHoraria = true;
                    resultadoSaveNovaCargaHoraria.setValue("A carga horária foi alterada com sucesso!");
                }else{
                    podeMostrarSnackDeSaveDeNovaCargaHoraria = true;
                    resultadoSaveNovaCargaHoraria.setValue("Ocorreu um erro ao salvar a nova carga horária. Tente Novamente!");
                }
            }
        });

    }
}
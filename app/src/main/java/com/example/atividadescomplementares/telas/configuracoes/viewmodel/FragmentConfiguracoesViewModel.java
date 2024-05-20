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


/**
 * viewModel para ponte entre dados e tela.
 *
 * Nessa viewModel em específico, operações de configurações são requisitadas
 * a camada de dados, quando as operações são concluidas, a tela fica sabendo
 * através dos observaveis(objetos do tipo LiveData)
 */
public class FragmentConfiguracoesViewModel extends ViewModel {

    //as proximas duas variaveis representam o valor do email e carga que o usuario já tem cadastrado
    //elas são inicializadas com empty string e 0 porque esses valores cadastrados ainda não foram obtidos
    //nesse momento.

    //essas variaveis tambem servem para comparação quando o user quiser cadastrar carga ou email novos
    //se ele colocar campos iguais ao que ja estao cadastrados, a alteração não vai ser feita(afinal não tem nada para ser alterado)
    public String emailInicial = "";
    public Integer cargaNecessariaInicial = 0;


    //objeto liveData que pode ser observado pela tela(fragment).
    public MutableLiveData<Integer> cargaTotalNecessaria = new MutableLiveData<>();


    //instancia para acesso aos metodos do firebase
    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();

    //instancia para acesso aos metodos do firebase

    private UserFirebase userFirebase = new UserFirebase();

    //instancia para acesso aos metodos do firebase

    //objeto liveData que pode ser observado pela tela(fragment)
    private RecuperarSenhaFirebase recuperarSenhaFirebase = new RecuperarSenhaFirebase();

    //objeto liveData que pode ser observado pela tela(fragment)

    public MutableLiveData<String> resultadoEnvioEmailRecuperacao = new MutableLiveData<>();

    //objeto liveData que pode ser observado pela tela(fragment)

    public MutableLiveData<String> resultadoSaveNovoEmail = new MutableLiveData<>();

    //objeto liveData que pode ser observado pela tela(fragment)

    public MutableLiveData<String> resultadoSaveNovaCargaHoraria = new MutableLiveData<>();


    /**
     * uma explicação melhor para esses objetos liveData:
     *
     * Quando esses objetos liveData recebem um novo valor, a tela sera avisada de que um novo valor
     * esta disponivel e a partir disso conseguirá tomar os procedimentos necessarios(mostrar uma mensagem na tela, iniciar outro metodo etc)
     */


    /**
     * as tres variaveis a seguir servem para impedir que a mensagem
     * seja mostrada na tela em momentos que não precisam ser mostradas.
     *
     * Por exemplo, quando eu altero o email a mensagem deve ser mostrada,
     * porem quando o user altera o email, sai da tela e volta para a tela de configuracoes,
     * a mensagem nao deve aparecer nesse caso(a mensagem apareceria denovo pois o livedata salva o estado salvo anteriormente)
     *
     * Para mais informações dê uma olhada na documentação de livedata do android
     */
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


    /*
    para entender o que os metodos a seguir fazem leia o nome da função
     */

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

    //Db quer dizer banco de dados
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
package com.example.atividadescomplementares.telas.cadastro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.cadastro.CadastroCallback;
import com.example.atividadescomplementares.dados.cadastro.CadastroFirebase;
import com.example.atividadescomplementares.dados.cadastro.CadastroNoDBCallback;


/**
 * viewModel de cadastro. Essa viewModel serve de ponte entre os dados e a tela
 *
 * Nessa viewModel conseguimos acessar as classes de acesso ao firebase, obter o resultado e passá-los para a tela
 */
public class FragmentCadastroViewModel extends ViewModel {

    //variaveis de controle do olho de mostrar senha
    public boolean olhoDaSenhaAberto = false;
    public boolean olhoDaConfirmaSenhaAberto = false;


    //objeto observavel que possibilita a tela observar o momento em que o dado é obtido
    //a fim de atualizar a tela quando o resultado for obtido
    public MutableLiveData<String> resultadoCadastro = new MutableLiveData<>();


    //instancia das classes referentes ao cadastro do user
    private CadastroFirebase cadastroFirebase = new CadastroFirebase();
    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();



    //metodo que é chamado quando o user clica para fazer o cadastro e ja preencheu os campos
    public void cadastrarUsuario(String nome, String email, int cargaHoraria, String senha) {
        cadastroFirebase.cadastrarNovoUsuario(email, senha, new CadastroCallback() {
            @Override
            public void onCadastrou(boolean sucesso) {
                if(sucesso){
                    //cadastro bem sucedido
                    //continua para cadastrar a carga horaria no db do user
                    cadastrarNovoUserNoDatabase(nome, email, cargaHoraria);
                }else{


                    //retorna um erro avisando que houve um erro no cadastro
                    resultadoCadastro.setValue("Houve um erro no cadastro. Tente novamente!");

                }
            }
        });

    }


    //depois que o cadastro foi feito com sucesso, esse metodo é chamado para salvar algumas
    //informacoes do user no database
    private void cadastrarNovoUserNoDatabase(String nome, String email, int cargaHoraria) {
        persistenciaFirebase.salvarInformacoesDoNovoUsuario(nome, email, cargaHoraria, new CadastroNoDBCallback() {
            @Override
            public void onCadastrouUserNoFirebase(boolean salvouInfoDoUser) {
                resultadoCadastro.setValue("Cadastro efetuado com sucesso!");

            }
        });
    }
}
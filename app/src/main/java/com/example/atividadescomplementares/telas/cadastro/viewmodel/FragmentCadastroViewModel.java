package com.example.atividadescomplementares.telas.cadastro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.cadastro.CadastroCallback;
import com.example.atividadescomplementares.dados.cadastro.CadastroFirebase;
import com.example.atividadescomplementares.dados.cadastro.CadastroNoDBCallback;

public class FragmentCadastroViewModel extends ViewModel {
    public boolean olhoDaSenhaAberto = false;
    public boolean olhoDaConfirmaSenhaAberto = false;

    public MutableLiveData<String> resultadoCadastro = new MutableLiveData<>();

    private CadastroFirebase cadastroFirebase = new CadastroFirebase();
    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();


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

    private void cadastrarNovoUserNoDatabase(String nome, String email, int cargaHoraria) {
        persistenciaFirebase.salvarInformacoesDoNovoUsuario(nome, email, cargaHoraria, new CadastroNoDBCallback() {
            @Override
            public void onCadastrouUserNoFirebase(boolean salvouInfoDoUser) {
                resultadoCadastro.setValue("Cadastro efetuado com sucesso!");

            }
        });
    }
}
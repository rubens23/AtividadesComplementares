package com.example.atividadescomplementares.telas.formulario.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.atividadeComplementar.SalvouAtividadeCallback;


/**
 * viewModel que faz a ponte entre a tela de formulario
 * e as classes de acesso aos dados necessarios nessa tela
 */
public class FragmentFormularioViewModel extends ViewModel {

    //o spinner de modalidades começa com a opção 'Ensino' selecionada
    public String selectedItemModalidades = "Ensino";

    //o spinner de atividades começa com a opção 'Cursos de idioma' selecionada
    public String selectedItemAtividades = "Cursos de idioma";

    //objeto livedata para o fragment observar mudanças referentes ao save da atividade complementar
    public MutableLiveData<String> resultadoSaveAtividadeComplementar = new MutableLiveData<>();

    //variavel para controlar se snackbar deve ser mostrada ou nao
    public boolean podeMostrarResultSnackbar = false;

    //array de modalidades
    public int arrayModalidades = R.array.modalidades;

    //array inicial de atividades. Essa array muda de acordo com a modalidade escolhida
    public int arrayAtividades = R.array.atividadesEnsino;

    //instancia da classe de acesso aos metodos do firebase
    PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();


    //metodo para cadastrar nova atividade complementar
    public void cadastrarNovaAtividade(int cargaHoraria, String descricao, String local) {
        persistenciaFirebase.salvarNovaAtividadeComplementar(new AtividadeComplementar(selectedItemModalidades,
                        selectedItemAtividades,
                        cargaHoraria,
                        descricao,
                        local),
                new SalvouAtividadeCallback() {
                    @Override
                    public void salvouNovaAtividade(boolean salvou) {
                        podeMostrarResultSnackbar = true;
                        if (salvou){
                            resultadoSaveAtividadeComplementar.setValue("atividade complementar salva com sucesso!");


                        }else {
                            resultadoSaveAtividadeComplementar.setValue("houve uma falha ao cadastrar a atividade complementar. Tente novamente!");

                        }

                    }
                });

    }


    //quando uma modalidade nova é escolhida, esse metodo é chamado
    //e coloca a array de atividades correta para aquela modalidade
    public void onModalidadeEscolhida() {
        switch (selectedItemModalidades){
            case "Ensino":
                arrayAtividades = R.array.atividadesEnsino;
                break;
            case "Pesquisa(IC)":
                arrayAtividades = R.array.atividadesPesquisa;
                break;
            case "Extensão":
                arrayAtividades = R.array.atividadesExtensao;
                break;
            case "Esporte, Arte e Cultura":
                arrayAtividades = R.array.atividadesEsporteArteCultura;
                break;
            case "Cidadania, Sustentabilidade e Empregabilidade":
                arrayAtividades = R.array.atividadesCidadaniaSustentabilidadeEmpregabilidade;
                break;
        }
    }


}
package com.example.atividadescomplementares.telas.formulario.viewmodel;

import android.annotation.SuppressLint;

import androidx.annotation.ArrayRes;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.atividadeComplementar.SalvouAtividadeCallback;

public class FragmentFormularioViewModel extends ViewModel {
    public String selectedItemModalidades = "Ensino";
    public String selectedItemAtividades = "Cursos de idioma";

    public MutableLiveData<String> resultadoSaveAtividadeComplementar = new MutableLiveData<>();

    public boolean podeMostrarResultSnackbar = false;

    public int arrayModalidades = R.array.modalidades;

    public int arrayAtividades = R.array.atividadesEnsino;

    PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();

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
package com.example.atividadescomplementares.telas.editarAtividadeComplementar;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.AtualizouAtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;

public class FragmentEditarAtividadeComplementarViewModel extends AndroidViewModel {

    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();

    public MutableLiveData<String> resultadoUpdateAtividadeComplementar = new MutableLiveData<>();

    public boolean podeMostrarResultSnackbar = false;


    public AtividadeComplementar atividadeComplementarAtual;


    public int arrayModalidades = R.array.modalidades;

    public int arrayAtividades = R.array.atividadesEnsino;

    public String selectedItemModalidades = "";

    public String selectedItemAtividades = "";

    public int cargaHoraria = 0;

    public String descricaoAtividade = "";

    public String localAtividade = "";

    public FragmentEditarAtividadeComplementarViewModel(@NonNull Application application) {
        super(application);
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

    public int findArrayPosition(int arrayResourceId, String value) {
        String[] array = getApplication().getResources().getStringArray(arrayResourceId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }


    public void atualizarAtividade() {
        atividadeComplementarAtual.atividade = selectedItemAtividades;
        atividadeComplementarAtual.modalidade = selectedItemModalidades;
        atividadeComplementarAtual.cargaHoraria = cargaHoraria;
        atividadeComplementarAtual.descricaoAtividade = descricaoAtividade;
        atividadeComplementarAtual.local = localAtividade;

        Log.d("testeSaveUpdate", "atividade complementar atualizada "+atividadeComplementarAtual.modalidade+atividadeComplementarAtual.atividade+atividadeComplementarAtual.descricaoAtividade+atividadeComplementarAtual.cargaHoraria+atividadeComplementarAtual.local );
        persistenciaFirebase.alterarAtividadeComplementar(atividadeComplementarAtual, new AtualizouAtividadeComplementar() {
            @Override
            public void atualizouAtividadeComplementar(boolean atualizou) {
                podeMostrarResultSnackbar = true;
                if(atualizou){
                    resultadoUpdateAtividadeComplementar.setValue("atualizou");
                }else{
                    resultadoUpdateAtividadeComplementar.setValue("não atualizou");
                }

            }
        });
    }
}
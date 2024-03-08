package com.example.atividadescomplementares.telas.home.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.PegouListaDeAtividadesComplementares;
import com.example.atividadescomplementares.dados.atividadeComplementar.PersistenciaFirebase;
import com.example.atividadescomplementares.dados.user.PegouCargaHorariaTotal;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeViewModel extends ViewModel {

    public boolean showChips = false;
    public boolean showGrafico = true;

    public MutableLiveData<List<AtividadeComplementar>> listaDeAtividades = new MutableLiveData<>();
    public MutableLiveData<List<AtividadeComplementar>> listaDeAtividadesPorModalidade = new MutableLiveData<>();
    public MutableLiveData<String> cargaTotalLD = new MutableLiveData<>();
    public MutableLiveData<String> cargaTotalObtida = new MutableLiveData<>();
    public MutableLiveData<Integer> porcentagemHorasConcluidas = new MutableLiveData<>();


    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();
    public void pegarListaDeAtividades() {
        persistenciaFirebase.pegarAtividadesComplementaresPorUser(new PegouListaDeAtividadesComplementares() {
            @Override
            public void pegouLista(List<AtividadeComplementar> lista) {
                listaDeAtividades.setValue(lista);

            }
        });
    }

    public void calcularHorasRestantes(List<AtividadeComplementar> atividadeComplementars) {
        //pegar carga total do user
        persistenciaFirebase.pegarCargaTotalNecessaria(new PegouCargaHorariaTotal() {
            @Override
            public void pegouCargaTotal(Integer cargaTotal) {
                int somaCargaAtualObtida = 0;
                for (AtividadeComplementar atividade: atividadeComplementars){
                    if(atividade != null){
                        somaCargaAtualObtida = somaCargaAtualObtida + atividade.cargaHoraria;

                    }


                }
                //retornar carga total
                cargaTotalLD.setValue(cargaTotal.toString());
                //retornar carga total obtida
                cargaTotalObtida.setValue(String.valueOf(somaCargaAtualObtida));
                fazerCalculoParaCircularProgressIndicator(Integer.parseInt(String.valueOf(cargaTotal)), somaCargaAtualObtida);
            }
        });
        //somar carga total obtida pelas atividades
    }

    private void fazerCalculoParaCircularProgressIndicator(int cargaTotal, int cargaAtualObtida) {
        int totalDeHorasNecessarias = cargaTotal;
        int totalDeHorasObtidas = cargaAtualObtida;

        porcentagemHorasConcluidas.setValue((100 * totalDeHorasObtidas) / totalDeHorasNecessarias);


    }

    public void pegarListaDeAtividadesPorModalidade(String modalidade) {
        switch (modalidade){
            case "ensino":
                pegarAtividadesPorModalidade("Ensino");

                break;
            case "pesquisa":
                pegarAtividadesPorModalidade("Pesquisa(IC)");

                break;
            case "extensao":
                pegarAtividadesPorModalidade("Extensão");

                break;
            case "esporte":
                pegarAtividadesPorModalidade("Esporte, Arte e Cultura");

                break;
            case "cidadania":
                pegarAtividadesPorModalidade("Cidadania, Sustentabilidade e Empregabilidade");

                break;
            case "todos":
                pegarAtividadesPorModalidade("todos");

                break;
        }
    }

    private void pegarAtividadesPorModalidade(String modalidade){
        persistenciaFirebase.pegarAtividadesComplementaresPorUserEModalidade(modalidade, new PegouListaDeAtividadesComplementares() {
            @Override
            public void pegouLista(List<AtividadeComplementar> lista) {
                listaDeAtividadesPorModalidade.setValue(lista);


            }
        });
    }
}
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


/**
 * viewModel da pagina inicial. Faz a ponte entre os dados que estão no firebase e a tela,
 * fazendo com que o fragment só lide com a lógica relacionada às views do xml
 * e não tendo que lidar com uma camada a qual ele não precisa ter contato direto
 */
public class FragmentHomeViewModel extends ViewModel {


    //'Chip' é o nome daquele item que é usado no filtro que serve para filtrar por modalidade
    //essas variaveis controlam se o grafico ou os Chips devem ser mostrados ou não
    public boolean showChips = false;
    public boolean showGrafico = true;


    //a seguir alguns liveDatas que podem ser observados a partir do fragment
    public MutableLiveData<List<AtividadeComplementar>> listaDeAtividades = new MutableLiveData<>();
    public MutableLiveData<List<AtividadeComplementar>> listaDeAtividadesPorModalidade = new MutableLiveData<>();
    public MutableLiveData<String> cargaTotalLD = new MutableLiveData<>();
    public MutableLiveData<String> cargaTotalObtida = new MutableLiveData<>();
    public MutableLiveData<Integer> porcentagemHorasConcluidas = new MutableLiveData<>();


    //instancia de uma classe com metodos de acesso ao firebase
    private PersistenciaFirebase persistenciaFirebase = new PersistenciaFirebase();


    //esse metodo pega a lista de atividades complementares
    public void pegarListaDeAtividades() {
        persistenciaFirebase.pegarAtividadesComplementaresPorUser(new PegouListaDeAtividadesComplementares() {
            @Override
            public void pegouLista(List<AtividadeComplementar> lista) {
                listaDeAtividades.setValue(lista);

            }
        });
    }


    //esse metodo faz o calculo da quantidade horas restantes para completar as horas complementares
    public void calcularHorasRestantes(List<AtividadeComplementar> atividadeComplementars) {
        //pegar carga total do user
        persistenciaFirebase.pegarCargaTotalNecessaria(new PegouCargaHorariaTotal() {
            @Override
            public void pegouCargaTotal(Integer cargaTotal) {
                int somaCargaAtualObtida = 0;
                //esse for pega cada atividade complementar para somar a carga horaria de cada uma e obter a
                //quantidade de horas complementares já completadas
                for (AtividadeComplementar atividade: atividadeComplementars){
                    if(atividade != null){
                        somaCargaAtualObtida = somaCargaAtualObtida + atividade.cargaHoraria;

                    }


                }
                //retorna carga total necessaria
                cargaTotalLD.setValue(cargaTotal.toString());
                //retornar carga total obtida(quantas horas o user ja completou)
                cargaTotalObtida.setValue(String.valueOf(somaCargaAtualObtida));
                fazerCalculoParaCircularProgressIndicator(Integer.parseInt(String.valueOf(cargaTotal)), somaCargaAtualObtida);
            }
        });
    }

    //aqui o caluclo que vai alimentar o grafico sera feito. Um valor é fornecido para preencher o grafico
    //com as horas ja completadas(rosa para ja completado e preto para ainda falta)
    private void fazerCalculoParaCircularProgressIndicator(int cargaTotal, int cargaAtualObtida) {
        int totalDeHorasNecessarias = cargaTotal;
        int totalDeHorasObtidas = cargaAtualObtida;

        porcentagemHorasConcluidas.setValue((100 * totalDeHorasObtidas) / totalDeHorasNecessarias);


    }


    //quando o chip for escolhido isso sera usado para pegar as atividades por modalidade
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


    //metodo para pegar as atividades por modalidade
    private void pegarAtividadesPorModalidade(String modalidade){
        persistenciaFirebase.pegarAtividadesComplementaresPorUserEModalidade(modalidade, new PegouListaDeAtividadesComplementares() {
            @Override
            public void pegouLista(List<AtividadeComplementar> lista) {
                listaDeAtividadesPorModalidade.setValue(lista);


            }
        });
    }
}
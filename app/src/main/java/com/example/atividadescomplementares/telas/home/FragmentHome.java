package com.example.atividadescomplementares.telas.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.databinding.FragmentHomeBinding;
import com.example.atividadescomplementares.telas.home.viewmodel.FragmentHomeViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {


    //instancia da viewModel
    private FragmentHomeViewModel mViewModel;

    //instancia do binding
    private FragmentHomeBinding binding;

    //observer que observa o resultado da lista de atividades complementares
    Observer<List<AtividadeComplementar>> resultadoListaDeAtividades = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            fazerOCalculoDoTotalDeHorasRestantes(atividadeComplementars);
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };


    //observer que observa o resultado da lista de atividade complementares por modalidade
    Observer<List<AtividadeComplementar>> resultadoListaDeAtividadesPorModalidade = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };


    //observer que observa o resultado da carga total
    Observer<String> resultadoCargaTotal = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotal) {
            setarCargaTotalNaView(cargaTotal);


        }

    };



    //observer que observa o resultado da carga horaria obtida até o momento(quantas horas o user já completou)
    Observer<String> resultadoCargaTotalObtida = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotalObtida) {
            setarCargaTotalObtidaNaView(cargaTotalObtida);



        }

    };


    //observer que observa a porcentagem total de horas que servirá para preencher o gráfico
    Observer<Integer> porcentagemTotalDeHoras = new Observer<Integer>() {
        @Override
        public void onChanged(Integer porcentagem) {
            setPorcentagemNoCircularProgressIndicator(porcentagem);



        }

    };

    /*
    observação sobre os observers acima:

    cada observer desse vai receber um valor assim que ele for chamado. O resultado de algo é o valor recebido la da viewModel.

    O fluxo é mais ou menos o seguinte:
    fragment -> chama viewModel para pegar dados -> viewModel chama os metodos do firebase p/ pegar os dados -> dados são obtidos -> viewModel é avisada atraves de callbacks -> viewModel coloca o valor no liveData -> fragment que estava observando pega o novo valor do liveData
     */



    //para explicacao sobre esse metodo visite o comentario do onCreateView do FragmentBoasVindas

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentHomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    //para explicacao sobre esse metodo visite o comentario do onViewCreated do FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObservers();
        mViewModel.pegarListaDeAtividades();
        fazerSelecaoInicialDoChip();
        onClickListeners();
    }

    private void fazerSelecaoInicialDoChip() {
        binding.chipTodos.setChecked(true);
        binding.containerChipGroup.post(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.containerChipGroup.smoothScrollTo(binding.chipTodos.getLeft(), binding.chipTodos.getTop());
                    }
                }
        );
    }

    //preenche o grafico de acordo com a porcentagem de horas concluidas
    private void setPorcentagemNoCircularProgressIndicator(Integer porcentagem) {
        int progresso = Math.min(100, porcentagem);


        binding.indicadorCircularDeModalidade.setProgress(progresso);
    }

    //seta uma das textviews que fica dentro do grafico

    private void setarCargaTotalObtidaNaView(String cargaTotalObtida) {
        Integer validNumber = mViewModel.tryParseInt(cargaTotalObtida);
        if(validNumber != null){
            boolean jaAtingiuTotalDeCargaHoraria = mViewModel.checarSeJaAtingiuTotalDeCargaHoraria(validNumber);
            if(!jaAtingiuTotalDeCargaHoraria){
                binding.cargaTotalObtida.setText(cargaTotalObtida);
                mViewModel.completouHorasComplementares = false;
                showCompletouGrafico(false);
            }else{

                if(mViewModel.mCargaTotal > 0){
                    binding.cargaTotalObtida.setText(mViewModel.mCargaTotal.toString());
                    mViewModel.completouHorasComplementares = true;
                    showCompletouGrafico(true);
                }
            }

        }

    }

    //seta uma das textviews que fica dentro do grafico

    @SuppressLint("SetTextI18n")
    private void setarCargaTotalNaView(String cargaTotal) {
        binding.cargaTotal.setText("/"+cargaTotal+"h");
    }


    //pega o calculo de quantas horas faltam para o user completar as atividades complementares
    private void fazerOCalculoDoTotalDeHorasRestantes(List<AtividadeComplementar> atividadeComplementars) {
        mViewModel.calcularHorasRestantes(atividadeComplementars);
    }


    //coloca as atividades complementares na recycler view
    private void colocarAtividadesNaRecyclerView(List<AtividadeComplementar> atividadeComplementars) {
        ArrayList<AtividadeComplementar> listaAdapter = new ArrayList<>();
        for (AtividadeComplementar atividade : atividadeComplementars){
            if(atividade != null){
                listaAdapter.add(atividade);
            }
        }
        AtividadeAdapter adapter = new AtividadeAdapter(listaAdapter);
        binding.rvAtividadesComplementares.setAdapter(adapter);
    }



    private void initObservers() {
        mViewModel.listaDeAtividades.observe(requireActivity(), resultadoListaDeAtividades);
        mViewModel.cargaTotalLD.observe(requireActivity(), resultadoCargaTotal);
        mViewModel.cargaTotalObtida.observe(requireActivity(), resultadoCargaTotalObtida);
        mViewModel.porcentagemHorasConcluidas.observe(requireActivity(), porcentagemTotalDeHoras);
        mViewModel.listaDeAtividadesPorModalidade.observe(requireActivity(), resultadoListaDeAtividadesPorModalidade);
    }

    private void onClickListeners() {
        binding.btnMostrarChips.setOnClickListener(v->{
            //controla a visibilidade dos Chips
            if(mViewModel.showChips){
                binding.containerChipGroup.setVisibility(View.GONE);
                mViewModel.showChips = false;
                animarFiltroParaBaixo(binding.btnMostrarChips);

            }else {
                binding.containerChipGroup.setVisibility(View.VISIBLE);
                mViewModel.showChips = true;
                animarFiltroParaCima(binding.btnMostrarChips);


            }

        });


        //controla a visibilidade do grafico
        binding.btnMostrarGrafico.setOnClickListener(v->{
            if(mViewModel.showGrafico){
                binding.containerTextoGrafico.setVisibility(View.GONE);
                binding.indicadorCircularDeModalidade.setVisibility(View.GONE);
                binding.labelGrafico.setVisibility(View.INVISIBLE);
                mViewModel.showGrafico = false;
                animarSetaGraficoParaBaixo(binding.btnMostrarGrafico);
                showCompletouGrafico(false);

            }else{
                binding.containerTextoGrafico.setVisibility(View.VISIBLE);
                binding.indicadorCircularDeModalidade.setVisibility(View.VISIBLE);
                binding.labelGrafico.setVisibility(View.VISIBLE);
                mViewModel.showGrafico = true;
                animarSetaGraficoParaCima(binding.btnMostrarGrafico);
                showCompletouGrafico(true);
            }
        });


        //listener para cada Chip. Quando clicar no chip, a lista de atividades filtrada por modalidade será exibida
        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                if(checkedIds.size() == 0){
                    mViewModel.pegarListaDeAtividadesPorModalidade("todos");
                    mudarTextViewCategoria("TOTAL");



                }else {
                    if(checkedIds.get(0) == binding.chipEnsino.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("ensino");
                        mudarTextViewCategoria("Ensino");

                    }
                    if(checkedIds.get(0) == binding.chipPesquisa.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("pesquisa");
                        mudarTextViewCategoria("Pesquisa");


                    }
                    if(checkedIds.get(0) == binding.chipExtensao.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("extensao");
                        mudarTextViewCategoria("Extensão");


                    }
                    if(checkedIds.get(0) == binding.chipEsporteArteCultura.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("esporte");
                        mudarTextViewCategoria("Esporte");


                    }
                    if(checkedIds.get(0) == binding.chipCidadania.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("cidadania");
                        mudarTextViewCategoria("Cidadania");


                    }
                    if(checkedIds.get(0) == binding.chipTodos.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("todos");
                        mudarTextViewCategoria("TOTAL");


                    }
                }
            }
        });
    }

    private void mudarTextViewCategoria(String str) {
        binding.labelGrafico.setText(str);
    }

    private void showCompletouGrafico(boolean mostrar) {
        if(mViewModel.completouHorasComplementares){
            if(mostrar){
                binding.cvCompletouAtividades.setVisibility(View.VISIBLE);
            }else {
                binding.cvCompletouAtividades.setVisibility(View.GONE);
            }
        }else{
            binding.cvCompletouAtividades.setVisibility(View.GONE);

        }
    }


    /**
     *
     * a seguir alguns metodos para animação da seta de mostrar/esconder grafico/chips
     */
    private void animarSetaGraficoParaCima(ImageButton btnMostrarGrafico) {
        btnMostrarGrafico.animate().rotationBy(180f).setDuration(50).start();

    }

    private void animarSetaGraficoParaBaixo(ImageButton btnMostrarGrafico) {
        btnMostrarGrafico.animate().rotationBy(-180f).setDuration(50).start();


    }

    private void animarFiltroParaCima(ImageButton image){
        image.animate().rotationBy(180f).setDuration(50).start();



    }

    private void animarFiltroParaBaixo(ImageButton image){
        image.animate().rotationBy(-180f).setDuration(50).start();



    }


    //destruir os observers ao destruir o fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.listaDeAtividades.removeObserver(resultadoListaDeAtividades);
        mViewModel.cargaTotalLD.removeObserver(resultadoCargaTotal);
        mViewModel.cargaTotalObtida.removeObserver(resultadoCargaTotalObtida);
        mViewModel.porcentagemHorasConcluidas.removeObserver(porcentagemTotalDeHoras);
        mViewModel.listaDeAtividadesPorModalidade.removeObserver(resultadoListaDeAtividadesPorModalidade);

    }
}
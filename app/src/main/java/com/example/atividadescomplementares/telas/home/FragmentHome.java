package com.example.atividadescomplementares.telas.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    private FragmentHomeViewModel mViewModel;

    private FragmentHomeBinding binding;

    Observer<List<AtividadeComplementar>> resultadoListaDeAtividades = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            fazerOCalculoDoTotalDeHorasRestantes(atividadeComplementars);
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };

    Observer<List<AtividadeComplementar>> resultadoListaDeAtividadesPorModalidade = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };

    Observer<String> resultadoCargaTotal = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotal) {
            setarCargaTotalNaView(cargaTotal);


        }

    };



    Observer<String> resultadoCargaTotalObtida = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotalObtida) {
            setarCargaTotalObtidaNaView(cargaTotalObtida);



        }

    };

    Observer<Integer> porcentagemTotalDeHoras = new Observer<Integer>() {
        @Override
        public void onChanged(Integer porcentagem) {
            setPorcentagemNoCircularProgressIndicator(porcentagem);



        }

    };



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentHomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObservers();
        mViewModel.pegarListaDeAtividades();
        onClickListeners();
    }

    private void setPorcentagemNoCircularProgressIndicator(Integer porcentagem) {
        int progresso = Math.min(100, porcentagem);


        binding.indicadorCircularDeModalidade.setProgress(progresso);
    }


    private void setarCargaTotalObtidaNaView(String cargaTotalObtida) {
        binding.cargaTotalObtida.setText(cargaTotalObtida);
    }

    @SuppressLint("SetTextI18n")
    private void setarCargaTotalNaView(String cargaTotal) {
        binding.cargaTotal.setText("/"+cargaTotal+"h");
    }

    private void fazerOCalculoDoTotalDeHorasRestantes(List<AtividadeComplementar> atividadeComplementars) {
        mViewModel.calcularHorasRestantes(atividadeComplementars);
    }

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

        binding.btnMostrarGrafico.setOnClickListener(v->{
            if(mViewModel.showGrafico){
                binding.containerTextoGrafico.setVisibility(View.GONE);
                binding.indicadorCircularDeModalidade.setVisibility(View.GONE);
                binding.labelGrafico.setVisibility(View.INVISIBLE);
                mViewModel.showGrafico = false;
                animarSetaGraficoParaBaixo(binding.btnMostrarGrafico);
            }else{
                binding.containerTextoGrafico.setVisibility(View.VISIBLE);
                binding.indicadorCircularDeModalidade.setVisibility(View.VISIBLE);
                binding.labelGrafico.setVisibility(View.VISIBLE);
                mViewModel.showGrafico = true;
                animarSetaGraficoParaCima(binding.btnMostrarGrafico);
            }
        });

        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                if(checkedIds.size() == 0){
                    mViewModel.pegarListaDeAtividadesPorModalidade("todos");


                }else {
                    if(checkedIds.get(0) == binding.chipEnsino.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("ensino");

                    }
                    if(checkedIds.get(0) == binding.chipPesquisa.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("pesquisa");


                    }
                    if(checkedIds.get(0) == binding.chipExtensao.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("extensao");


                    }
                    if(checkedIds.get(0) == binding.chipEsporteArteCultura.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("esporte");


                    }
                    if(checkedIds.get(0) == binding.chipCidadania.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("cidadania");


                    }
                    if(checkedIds.get(0) == binding.chipTodos.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("todos");


                    }
                }
            }
        });
    }

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
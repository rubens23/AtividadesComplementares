package com.example.atividadescomplementares.telas.editarAtividadeComplementar;

import androidx.annotation.ArrayRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.dados.atividadeComplementar.SerializadorAtividadeComplementar;
import com.example.atividadescomplementares.databinding.FragmentEditarAtividadeComplementarBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class FragmentEditarAtividadeComplementar extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentEditarAtividadeComplementarViewModel mViewModel;

    private FragmentEditarAtividadeComplementarBinding binding;


    Observer<String> resultadoUpdateAtividade = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarResultSnackbar){
                if(s == "atualizou"){
                    showSnackbar("A atividade foi atualizada com sucesso!");
                }
            }
        }
    };

    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(FragmentEditarAtividadeComplementarViewModel.class);

        binding = FragmentEditarAtividadeComplementarBinding.inflate(inflater, container, false);

        return binding.getRoot();





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pegarArgumentos();
        dadosIniciaisAtividadeComplementar();
        iniciarSpinners();
        colocarAtividadeNasViews();
        onClickListeners();
        initObservers();
    }

    private void initObservers() {
        mViewModel.resultadoUpdateAtividadeComplementar.observe(requireActivity(), resultadoUpdateAtividade);
    }

    private void dadosIniciaisAtividadeComplementar() {
        mViewModel.cargaHoraria = mViewModel.atividadeComplementarAtual.cargaHoraria;
        mViewModel.descricaoAtividade = mViewModel.atividadeComplementarAtual.descricaoAtividade;
        mViewModel.localAtividade = mViewModel.atividadeComplementarAtual.local;
    }

    private void onClickListeners() {
        binding.btnSalvarNovaCargaHoraria.setOnClickListener(v->{
            atualizarAtividadeComplementar("cargaHorariaAtividade");
        });
        binding.btnSalvarNovaDescricaoAtividade.setOnClickListener(v->{
            atualizarAtividadeComplementar("descricaoAtividade");
        });
        binding.btnSalvarNovoLocal.setOnClickListener(v->{
            atualizarAtividadeComplementar("localAtividade");
        });
        binding.btnSalvarNovaModalidade.setOnClickListener(v->{
            atualizarAtividadeComplementar("modalidadeAtividade");
        });
        binding.btnSalvarNovaAtividade.setOnClickListener(v->{
            atualizarAtividadeComplementar("atividade");
        });

    }

    private void atualizarAtividadeComplementar(String campoAtividade) {
        switch (campoAtividade){
            case "cargaHorariaAtividade":
                String novaCargaHoraria = binding.etNovaCargaHorariaAtividade.getText().toString();
                if(!novaCargaHoraria.equals("")){
                    if(Integer.parseInt(novaCargaHoraria) != mViewModel.cargaHoraria){
                        mViewModel.cargaHoraria = Integer.parseInt(novaCargaHoraria);
                        mViewModel.atualizarAtividade();
                    }
                }
                break;
            case "descricaoAtividade":
                String novaDescricaoAtividade = binding.etNovaDescricaoAtividade.getText().toString();
                if(!novaDescricaoAtividade.equals("")){
                    if(!novaDescricaoAtividade.equals(mViewModel.descricaoAtividade)){
                        Log.d("monitorandoUpdate", "novodescricaoAtividade: "+novaDescricaoAtividade + " descricao que está salvo atualmente: "+mViewModel.descricaoAtividade);

                        mViewModel.descricaoAtividade = novaDescricaoAtividade;
                        mViewModel.atualizarAtividade();

                    }
                }
                break;
            case "localAtividade":
                String novoLocalAtividade = binding.etNovoLocal.getText().toString();
                if(!novoLocalAtividade.equals("")){
                    if(!novoLocalAtividade.equals(mViewModel.localAtividade)){
                        mViewModel.localAtividade = novoLocalAtividade;
                        mViewModel.atualizarAtividade();
                        Log.d("monitorandoUpdate", "novoLocalAtividade: "+novoLocalAtividade + " local que está salvo atualmente: "+mViewModel.localAtividade);
                    }
                }
                break;
            case "modalidadeAtividade":
                if(!Objects.equals(mViewModel.selectedItemModalidades, "")){
                    if(!Objects.equals(mViewModel.selectedItemModalidades, mViewModel.atividadeComplementarAtual.modalidade))
                    mViewModel.atualizarAtividade();
                }
                break;
            case "atividade":
                if(!Objects.equals(mViewModel.selectedItemAtividades, "")){
                    if(!Objects.equals(mViewModel.selectedItemAtividades, mViewModel.atividadeComplementarAtual.atividade))
                        mViewModel.atualizarAtividade();
                }
                break;
        }
    }

    private void iniciarSpinners() {
        initSpinnerModalidades();
        initSpinnerAtividades(mViewModel.arrayAtividades);
    }

    private void initSpinnerAtividades(@ArrayRes int arrayAtividades) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayAtividades,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNovaAtividade.setAdapter(adapter);
        binding.spinnerNovaAtividade.setOnItemSelectedListener(this);
    }

    private void initSpinnerModalidades() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.modalidades,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNovaModalidade.setAdapter(adapter);
        binding.spinnerNovaModalidade.setOnItemSelectedListener(this);

    }

    private void colocarAtividadeNasViews() {
        mViewModel.selectedItemModalidades = mViewModel.atividadeComplementarAtual.modalidade;
        binding.spinnerNovaModalidade.setSelection(mViewModel.findArrayPosition(mViewModel.arrayModalidades, mViewModel.atividadeComplementarAtual.modalidade));
        mViewModel.onModalidadeEscolhida();
        initSpinnerAtividades(mViewModel.arrayAtividades);
        mViewModel.selectedItemAtividades = mViewModel.atividadeComplementarAtual.atividade;
        binding.spinnerNovaAtividade.setSelection(mViewModel.findArrayPosition(mViewModel.arrayAtividades, mViewModel.atividadeComplementarAtual.atividade));



        binding.etNovaCargaHorariaAtividade.setText(String.valueOf(mViewModel.atividadeComplementarAtual.cargaHoraria));
        binding.labelEditarAtividade.setText(mViewModel.atividadeComplementarAtual.atividade);
        binding.etNovaDescricaoAtividade.setText(mViewModel.atividadeComplementarAtual.descricaoAtividade);
        binding.etNovoLocal.setText(mViewModel.atividadeComplementarAtual.local);






    }

    private void pegarArgumentos() {
        if(getArguments() != null){
            String atividadeJson = getArguments().getString("atividadeJson");
            mViewModel.atividadeComplementarAtual = null;
            mViewModel.atividadeComplementarAtual = SerializadorAtividadeComplementar.deserializeAtividade(atividadeJson);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnerNovaModalidade) {
            mViewModel.selectedItemModalidades = adapterView.getItemAtPosition(i).toString();
            mViewModel.onModalidadeEscolhida();
            initSpinnerAtividades(mViewModel.arrayAtividades);
            if(mViewModel.findArrayPosition(mViewModel.arrayAtividades, mViewModel.selectedItemAtividades) != -1){
                binding.spinnerNovaAtividade.setSelection(mViewModel.findArrayPosition(mViewModel.arrayAtividades, mViewModel.selectedItemAtividades));


            }

        } else if (adapterView.getId() == R.id.spinnerNovaAtividade) {
            mViewModel.selectedItemAtividades = adapterView.getItemAtPosition(i).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.resultadoUpdateAtividadeComplementar.removeObserver(resultadoUpdateAtividade);
    }
}
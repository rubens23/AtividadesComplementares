package com.example.atividadescomplementares.telas.formulario;

import androidx.annotation.ArrayRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentFormularioBinding;
import com.example.atividadescomplementares.telas.formulario.viewmodel.FragmentFormularioViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FragmentFormulario extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentFormularioViewModel mViewModel;

    private FragmentFormularioBinding binding;

    Observer<String> resultadoSaveFormularioObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarResultSnackbar){
                showSnackbar(s);
                mViewModel.podeMostrarResultSnackbar = false;

            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentFormularioViewModel.class);

        binding = FragmentFormularioBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpinnerModalidades(mViewModel.arrayModalidades);
        initSpinnerAtividades(mViewModel.arrayAtividades);
        onCLickListeners();
        initObservers();

    }

    private void initObservers() {


        mViewModel.resultadoSaveAtividadeComplementar.observe(requireActivity(), resultadoSaveFormularioObserver);
    }

    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();

    }

    private void onCLickListeners() {
        binding.btnCadastrarNovaAtividade.setOnClickListener(v->{
            cadastrarNovaAtividade();
        });
    }

    private void cadastrarNovaAtividade() {
        boolean atividadeValida = true;
        String ch = binding.etCH.getText().toString();
        String descricao = binding.etDescricaoAtividade.getText().toString();
        String local = binding.etLocal.getText().toString();

        if (ch.isEmpty()){
            atividadeValida = false;
            binding.etCH.setError("Preencha o número da carga horária dessa atividade");
        }
        if(descricao.isEmpty()){
            atividadeValida = false;
            binding.etDescricaoAtividade.setError("Preencha a descrição dessa atividade. Ex.: Curso de Java");
        }
        if(local.isEmpty()){
            atividadeValida = false;
            binding.etLocal.setError("Preencha o local onde você concluiu essa atividade. Ex.: Alura");
        }


        if(atividadeValida){
            mViewModel.cadastrarNovaAtividade(Integer.parseInt(ch), descricao, local);
        }

    }

    private void initSpinnerAtividades(@ArrayRes int arrayAtividades) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayAtividades,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAtividades.setAdapter(adapter);
        binding.spinnerAtividades.setOnItemSelectedListener(this);
    }

    private void initSpinnerModalidades(@ArrayRes int arrayModalidades) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.modalidades,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerModalidades.setAdapter(adapter);
        binding.spinnerModalidades.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnerModalidades) {
            mViewModel.selectedItemModalidades = adapterView.getItemAtPosition(i).toString();
            mViewModel.onModalidadeEscolhida();
            initSpinnerAtividades(mViewModel.arrayAtividades);

        } else if (adapterView.getId() == R.id.spinnerAtividades) {
            mViewModel.selectedItemAtividades = adapterView.getItemAtPosition(i).toString();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.resultadoSaveAtividadeComplementar.removeObserver(resultadoSaveFormularioObserver);
    }
}




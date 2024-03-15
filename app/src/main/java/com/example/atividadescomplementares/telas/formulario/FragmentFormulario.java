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

    //instancia da viewModel
    private FragmentFormularioViewModel mViewModel;

    //instancia do binding
    private FragmentFormularioBinding binding;


    //observer para capturar as mudanças no resultado do save do formulario
    Observer<String> resultadoSaveFormularioObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarResultSnackbar){
                showSnackbar(s);
                mViewModel.podeMostrarResultSnackbar = false;

            }
        }
    };


    //para explicacao sobre esse metodo visite o comentario do onCreateView do FragmentBoasVindas

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentFormularioViewModel.class);

        binding = FragmentFormularioBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    //para explicacao sobre esse metodo visite o comentario do onViewCreated do FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //configurar o spinner de modalidades
        initSpinnerModalidades(mViewModel.arrayModalidades);
        //configurar o spinner de atividades
        initSpinnerAtividades(mViewModel.arrayAtividades);
        //onCLickListeners das views
        onCLickListeners();
        //init observers
        initObservers();

    }

    private void initObservers() {
        mViewModel.resultadoSaveAtividadeComplementar.observe(requireActivity(), resultadoSaveFormularioObserver);
    }

    //mostra uma mensagem na tela quando necessario
    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
        resetCamposFormulario();


    }


    //volta os campos do formulario para seu estado inicial após salvar uma nova atividade complementar
    private void resetCamposFormulario() {
        binding.etCH.setText("");
        binding.etDescricaoAtividade.setText("");
        binding.etLocal.setText("");
        binding.spinnerModalidades.setSelection(0);
        mViewModel.selectedItemModalidades = binding.spinnerModalidades.getItemAtPosition(0).toString();
        mViewModel.onModalidadeEscolhida();

    }

    private void onCLickListeners() {
        binding.btnCadastrarNovaAtividade.setOnClickListener(v->{
            //quando o botao cadastrar nova atividade for pressionado inicia-se validação do cadastro da nova atividade
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


        //se os campos do formularios foram preenchidos corretamente, inicia-se o save dessa atividade complementar
        if(atividadeValida){
            mViewModel.cadastrarNovaAtividade(Integer.parseInt(ch), descricao, local);
        }

    }

    //nesse metodo é passado uma lista de strings que vão ser as opções do spinner de atividades
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

    //nesse metodo é passado uma lista de strings que vão ser as opções do spinner de modalidades

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


    //quando um item(de qualquer um dos dois spinners) for selecionado esse metodo é acionado, para fazer as alterações
    //necessarias
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


    //metodo que é usado ao desselecionar o item do spinner
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //destrói os observers ao destruir o fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.resultadoSaveAtividadeComplementar.removeObserver(resultadoSaveFormularioObserver);
    }
}




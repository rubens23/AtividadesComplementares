package com.example.atividadescomplementares.telas.configuracoes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentConfiguracoesBinding;
import com.example.atividadescomplementares.telas.configuracoes.viewmodel.FragmentConfiguracoesViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FragmentConfiguracoes extends Fragment {

    private FragmentConfiguracoesViewModel mViewModel;

    private FragmentConfiguracoesBinding binding;

    private Observer<Integer> cargaTotalNecessaria = new Observer<Integer>() {
        @Override
        public void onChanged(Integer cargaTotalNecessaria) {
            mViewModel.cargaNecessariaInicial = cargaTotalNecessaria;
            setarCargaTotalNaEditText(cargaTotalNecessaria);

        }
    };

    Observer<String> resultadoEmailRecuperacao = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeRecuperacaoDeSenha){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeRecuperacaoDeSenha = false;

            }

        }
    } ;

    Observer<String> resultadoSaveNovoEmail = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeSaveDeNovoEmail){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeSaveDeNovoEmail = false;

            }

        }
    };

    Observer<String> resultadoSaveNovaCargaHoraria = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeSaveDeNovaCargaHoraria){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeSaveDeNovaCargaHoraria = false;

            }
        }
    };



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentConfiguracoesViewModel.class);

        binding = FragmentConfiguracoesBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObservers();
        pegarCargaTotal();
        pegarEmail();
        onClickListeners();

    }

    private void onClickListeners() {
        binding.btnMudarSenha.setOnClickListener(v->{
            mViewModel.enviarEmailDeRecuperacaoDeSenha();

        });

        binding.btnSalvarNovoEmail.setOnClickListener(v->{
            String email = binding.etNovoEmail.getText().toString();
            if(!email.equals(mViewModel.emailInicial) ){
                mostrarDialogDeReautenticacao(email);
            }


        });

        binding.btnSalvarNovaCargaHoraria.setOnClickListener(v->{
            Integer novaCargaHoraria = Integer.valueOf(binding.etCargaHoraria.getText().toString());
            if(!novaCargaHoraria.equals(mViewModel.cargaNecessariaInicial)){
                mViewModel.alterarCargaHorariaTotalDoUser(novaCargaHoraria);

            }


        });
    }

    private void mostrarDialogDeReautenticacao(String emailNovo) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_reautenticacao, null);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etSenha = view.findViewById(R.id.etSenha);

        etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Para alterar o e-mail se autentique novamente:")
                .setView(view)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = etEmail.getText().toString();
                        String senha = etSenha.getText().toString();
                        if(email.isEmpty() || senha.isEmpty()){
                            showSnackbar("Para mudar o email você precisa se reautenticar!");
                        }else{
                            mViewModel.salvarNovoEmail(email, senha, emailNovo);

                        }

                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();

    }


    private void initObservers() {
        mViewModel.cargaTotalNecessaria.observe(requireActivity(), cargaTotalNecessaria);
        mViewModel.resultadoEnvioEmailRecuperacao.observe(requireActivity(), resultadoEmailRecuperacao);
        mViewModel.resultadoSaveNovoEmail.observe(requireActivity(), resultadoSaveNovoEmail);
        mViewModel.resultadoSaveNovaCargaHoraria.observe(requireActivity(), resultadoSaveNovaCargaHoraria);

    }

    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();

    }


    private void pegarCargaTotal() {
        mViewModel.pegarCargaTotal();
    }

    private void pegarEmail() {
        String email = mViewModel.pegarEmail();
        mViewModel.emailInicial = email;
        setarEmailNaEditText(email);
    }

    private void setarCargaTotalNaEditText(Integer cargaTotalNecessaria) {
        binding.etCargaHoraria.setText(String.valueOf(cargaTotalNecessaria));
    }

    private void setarEmailNaEditText(String email){
        binding.etNovoEmail.setText(email);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.cargaTotalNecessaria.removeObserver(cargaTotalNecessaria);
        mViewModel.resultadoEnvioEmailRecuperacao.removeObserver(resultadoEmailRecuperacao);
        mViewModel.resultadoSaveNovoEmail.removeObserver(resultadoSaveNovoEmail);
        mViewModel.resultadoSaveNovaCargaHoraria.removeObserver(resultadoSaveNovaCargaHoraria);

    }
}
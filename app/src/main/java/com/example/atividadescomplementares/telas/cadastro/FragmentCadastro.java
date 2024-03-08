package com.example.atividadescomplementares.telas.cadastro;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentFragmentCadastroBinding;
import com.example.atividadescomplementares.telas.cadastro.viewmodel.FragmentCadastroViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FragmentCadastro extends Fragment {

    private FragmentCadastroViewModel mViewModel;

    private FragmentFragmentCadastroBinding binding;

    public static FragmentCadastro newInstance() {
        return new FragmentCadastro();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentCadastroViewModel.class);

        binding = FragmentFragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickListeners();
        initObservers();
    }

    private void initObservers() {
        mViewModel.resultadoCadastro.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showSnackbar(s);
                fecharFragment();


            }
        });
    }

    private void fecharFragment() {
        Navigation.findNavController(requireView()).popBackStack();
    }

    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
    }

    private void onClickListeners() {
        binding.olhoMostrarSenha.setOnClickListener(v->{
            if(mViewModel.olhoDaSenhaAberto){
                fecharOlhoDaSenha();
                esconderSenha();

            }else{
                abrirOlhoDaSenha();
                mostrarSenha();


            }
        });

        binding.olhoMostrarConfirmaSenha.setOnClickListener(v->{
            if(mViewModel.olhoDaConfirmaSenhaAberto){
                fecharOlhoDaConfirmaSenha();
                esconderConfirmaSenha();

            }else{
                abrirOlhoDaConfirmaSenha();
                mostrarConfirmaSenha();


            }
        });

        binding.btnCriarCadastro.setOnClickListener(v->{
            cadastrarUser();
        });

        binding.btnJaTemCadastro.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(FragmentCadastroDirections.actionFragmentCadastroToFragmentLogin());
        });


    }

    private void cadastrarUser() {
        String nome = binding.etNome.getText().toString();
        String email = binding.etEmail.getText().toString();
        String cargaHoraria = binding.etCargaHoraria.getText().toString();
        String senha = binding.etSenha.getText().toString();
        String confirmaSenha = binding.etConfirmeSenha.getText().toString();
        boolean cadastroValido = true;

        if (nome.isEmpty()) {
            cadastroValido = false;
            binding.etNome.setError("Digite um nome");
        }

        if (email.isEmpty()) {
            cadastroValido = false;
            binding.etEmail.setError("digite um email");
        }

        if (cargaHoraria.isEmpty()) {
            cadastroValido = false;
            binding.etCargaHoraria.setError("Digite a carga horária total de atividades complementares");
        }

        if (senha.isEmpty()) {
            cadastroValido = false;
            binding.etSenha.setError("Digite uma senha");
        } else if (senha.length() < 6) {
            cadastroValido = false;
            binding.etSenha.setError("Digite uma senha com pelo menos 6 digitos");
        } else if (!senha.equals(confirmaSenha)) {
            cadastroValido = false;
            binding.etSenha.setError("As senhas digitadas não são iguais");

        }

        if (cadastroValido) {
            mViewModel.cadastrarUsuario(nome,
                    email,
                    Integer.parseInt(cargaHoraria),
                    senha
                    );
        } else {
            // Alguma condição falhou, trate cada caso conforme necessário
        }
    }

    private void mostrarConfirmaSenha() {
        binding.etConfirmeSenha.setTransformationMethod(null);

    }

    private void esconderConfirmaSenha() {
        binding.etConfirmeSenha.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void mostrarSenha() {
        binding.etSenha.setTransformationMethod(null);

    }

    private void esconderSenha() {
        binding.etSenha.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void abrirOlhoDaConfirmaSenha() {
        binding.olhoMostrarConfirmaSenha.setImageResource(R.drawable.outline_remove_red_eye_24);
        mViewModel.olhoDaConfirmaSenhaAberto = true;
    }

    private void fecharOlhoDaConfirmaSenha() {
        binding.olhoMostrarConfirmaSenha.setImageResource(R.drawable.baseline_remove_red_eye_24);
        mViewModel.olhoDaConfirmaSenhaAberto = false;

    }

    private void abrirOlhoDaSenha() {
        binding.olhoMostrarSenha.setImageResource(R.drawable.outline_remove_red_eye_24);
        mViewModel.olhoDaSenhaAberto = true;

    }

    private void fecharOlhoDaSenha() {
        binding.olhoMostrarSenha.setImageResource(R.drawable.baseline_remove_red_eye_24);
        mViewModel.olhoDaSenhaAberto = false;
    }
}
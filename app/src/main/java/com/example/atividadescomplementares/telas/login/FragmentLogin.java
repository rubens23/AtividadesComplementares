package com.example.atividadescomplementares.telas.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentBoasVindasBinding;
import com.example.atividadescomplementares.databinding.FragmentLoginBinding;
import com.example.atividadescomplementares.telas.login.viewmodel.FragmentLoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FragmentLogin extends Fragment {


    //instancia da viewModel
    private FragmentLoginViewModel mViewModel;

    //instancia do binding
    private FragmentLoginBinding binding;


    //para mais informaçoes sobre esse metodo visite o mesmo metodo no FragmentBoasVindas
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentLoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //para mais informaçoes sobre esse metodo visite o mesmo metodo no FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickListeners();
        initObservers();
    }

    private void initObservers() {
        mViewModel.resultadoLogin.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == "login feito com sucesso"){
                    //assim que o resultado de login for observado e ele for de sucesso,
                    //usa o metodo do NavigationComponent para navegar para a tela de Home


                    Navigation.findNavController(requireView()).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentHome());

                }else {
                    showSnackbar(s);
                }

            }
        });
    }

    private void onClickListeners() {
        binding.btnCadastrar.setOnClickListener(v->{
            //navega para a tela de cadastro
            Navigation.findNavController(v).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentCadastro());
        });

        binding.olhoMostrarSenha.setOnClickListener(v->{
            //controla a visibilidade do olho de mostrar senha
            if(mViewModel.olhoDaSenhaAberto){
                fecharOlhoDaSenha();
                esconderSenha();

            }else{
                abrirOlhoDaSenha();
                mostrarSenha();


            }
        });

        binding.btnEsqueciSenha.setOnClickListener(v->{
            //navega para a tela de esqueceu senha
            Navigation.findNavController(v).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRecuperarSenha());
        });

        //ao clicar no login e os campos serem validos, tenta fazer login
        binding.btnLogin.setOnClickListener(v->{
            String email = binding.etEmail.getText().toString();
            String senha = binding.etSenha.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                showSnackbar("Preencha email e senha!");

            }else {
                mViewModel.fazerLogin(email, senha);
            }
        });
    }


    //mostra mensagens na tela quando necessário
    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
    }


    /**
     * todos os metodos abaixo são para controlar o comportamento do olho de mostrar senha
     *
     * para saber o que cada um faz leia o nome da função
     */
    private void fecharOlhoDaSenha() {
        binding.olhoMostrarSenha.setImageResource(R.drawable.baseline_remove_red_eye_24);
        mViewModel.olhoDaSenhaAberto = false;
    }

    private void esconderSenha() {
        binding.etSenha.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void abrirOlhoDaSenha() {
        binding.olhoMostrarSenha.setImageResource(R.drawable.outline_remove_red_eye_24);
        mViewModel.olhoDaSenhaAberto = true;

    }

    private void mostrarSenha() {
        binding.etSenha.setTransformationMethod(null);

    }


}
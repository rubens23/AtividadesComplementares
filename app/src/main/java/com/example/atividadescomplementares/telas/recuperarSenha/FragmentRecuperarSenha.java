package com.example.atividadescomplementares.telas.recuperarSenha;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentRecuperarSenhaBinding;
import com.example.atividadescomplementares.telas.recuperarSenha.viewmodel.FragmentRecuperarSenhaViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FragmentRecuperarSenha extends Fragment {


    //instancia da viewModel
    private FragmentRecuperarSenhaViewModel mViewModel;


    //instancia do binding
    private FragmentRecuperarSenhaBinding binding;


    //observer que observa mudanças no livedata que contem o resultado do envio do email de troca de senha
    Observer<String> resultadoEmailRecuperacao = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            showSnackbar(s);

        }
    } ;



    //para mais informaçoes sobre esse metodo visite o mesmo metodo no FragmentBoasVindas

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentRecuperarSenhaViewModel.class);

        binding = FragmentRecuperarSenhaBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }


    //para mais informaçoes sobre esse metodo visite o mesmo metodo no FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCLickListeners();
        initObservers();
    }

    private void initObservers() {
        mViewModel.resultadoEnvioEmailRecuperacao.observe(requireActivity(), resultadoEmailRecuperacao);
    }

    private void onCLickListeners() {
        binding.btnIrParaLogin.setOnClickListener(v->{
            //navega para a tela de login
            Navigation.findNavController(v).navigate(FragmentRecuperarSenhaDirections.actionFragmentRecuperarSenhaToFragmentLogin());
        });


        //se email for valido começa o processo de envio de email de troca de senha
        binding.btnRecuperarSenha.setOnClickListener(v->{
            String email = binding.etEmailRecuperacao.getText().toString();
            if(email.isEmpty()){
                binding.etEmailRecuperacao.setError("Preencha o e-mail!");
            }else{
                mViewModel.enviarEmailDeRecuperacaoDeSenha(email);
            }
        });
    }


    //msotra uma mensagem na tela quando necessário
    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();

    }



    //destroi os observers assim que o fragment for destruído
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.resultadoEnvioEmailRecuperacao.removeObserver(resultadoEmailRecuperacao);
    }
}
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

    private FragmentLoginViewModel mViewModel;

    private FragmentLoginBinding binding;

    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentLoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

//                    NavOptions navOptions = new NavOptions.Builder()
//                            .setPopUpTo(R.id.fragmentLogin, true)
//                                    .build();

                    //Navigation.findNavController(requireView()).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentHome(), navOptions);
                    Navigation.findNavController(requireView()).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentHome());

                }else {
                    showSnackbar(s);
                }

            }
        });
    }

    private void onClickListeners() {
        binding.btnCadastrar.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentCadastro());
        });

        binding.olhoMostrarSenha.setOnClickListener(v->{
            if(mViewModel.olhoDaSenhaAberto){
                fecharOlhoDaSenha();
                esconderSenha();

            }else{
                abrirOlhoDaSenha();
                mostrarSenha();


            }
        });

        binding.btnEsqueciSenha.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRecuperarSenha());
        });

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

    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
    }

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
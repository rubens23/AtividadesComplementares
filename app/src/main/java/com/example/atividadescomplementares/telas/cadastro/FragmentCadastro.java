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


/**
 * Fragment de cadastro. É o controlador do arquivo de xml de cadastro
 *
 * Os fragments obedecem o ciclo de vida da activity host. Quando a activity host for
 * destruida(quando o app for fechado) o fragment tambem é destruido
 */
public class FragmentCadastro extends Fragment {


    //instancia da viewModel
    private FragmentCadastroViewModel mViewModel;

    //instancia do binding de acesso ao xml
    private FragmentFragmentCadastroBinding binding;


    //para explicacao sobre esse metodo visite o comentario do onCreateView do FragmentBoasVindas
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentCadastroViewModel.class);

        binding = FragmentFragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    //para explicacao sobre esse metodo visite o comentario do onViewCreated do FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickListeners();
        //começa a observar o objeto que esta na viewModel
        //quando os valores dos objetos que estão na viewModel mudam, a view é avisada
        //e pode fazer as alterações necessarias na tela.
        initObservers();
    }

    private void initObservers() {

        //observa o resultado do cadastro e mostra uma mensagem na tela quando
        //o resultado do cadastro é obtido
        mViewModel.resultadoCadastro.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showSnackbar(s);
                //fecha o fragment depois do resultado do cadastro
                fecharFragment();


            }
        });
    }


    private void fecharFragment() {
        //usa o navigationComponent para fechar o fragment atual e voltar para o fragment anterior
        Navigation.findNavController(requireView()).popBackStack();
    }


    //esse metodo mostra uma mensagem na tela
    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
    }

    private void onClickListeners() {
        binding.olhoMostrarSenha.setOnClickListener(v->{
            //controle sobre o comportamento do olho de mostrar senha no campo de senha da tela de cadastro
            if(mViewModel.olhoDaSenhaAberto){
                fecharOlhoDaSenha();
                esconderSenha();

            }else{
                abrirOlhoDaSenha();
                mostrarSenha();


            }
        });

        //controle sobre o comportamento do olho de mostrar senha no campo de confirmar a senha da tela de cadastro

        binding.olhoMostrarConfirmaSenha.setOnClickListener(v->{
            if(mViewModel.olhoDaConfirmaSenhaAberto){
                fecharOlhoDaConfirmaSenha();
                esconderConfirmaSenha();

            }else{
                abrirOlhoDaConfirmaSenha();
                mostrarConfirmaSenha();


            }
        });


        //botao para iniciar o processo de cadastro do novo user
        binding.btnCriarCadastro.setOnClickListener(v->{
            cadastrarUser();
        });



        binding.btnJaTemCadastro.setOnClickListener(v->{
            //quando esse botao é clicado navegamos para a tela de login
            Navigation.findNavController(v).navigate(FragmentCadastroDirections.actionFragmentCadastroToFragmentLogin());
        });


    }



    private void cadastrarUser() {
        //variaveis que contem os valores dos campos de cadastro
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
            binding.etSenha.setError("Digite uma senha com pelo menos 6 dígitos");
        } else if (!senha.equals(confirmaSenha)) {
            cadastroValido = false;
            binding.etSenha.setError("As senhas digitadas não são iguais");

        }


        //se os campos de cadastro tiverem sido preenchidos corretamente, a viewModel é chamada para dar continuidade ao processo de cadastro
        if (cadastroValido) {
            mViewModel.cadastrarUsuario(nome,
                    email,
                    Integer.parseInt(cargaHoraria),
                    senha
                    );
        } else {
            // Alguma condição falhou, trate cada caso conforme necessário
            // se um controle maior for necessario, utilize esse bloco do else
        }
    }

    /**
     * todos os metodos a seguir são referentes ao controle do olho de mostrar senha
     * para saber o que cada metodo faz, leia o nome da função
     */

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
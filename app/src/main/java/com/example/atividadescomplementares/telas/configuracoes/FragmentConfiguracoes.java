package com.example.atividadescomplementares.telas.configuracoes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentConfiguracoesBinding;
import com.example.atividadescomplementares.telas.configuracoes.viewmodel.FragmentConfiguracoesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class FragmentConfiguracoes extends Fragment {


    //instancia da viewModel
    private FragmentConfiguracoesViewModel mViewModel;

    //instancia do viewBinding
    private FragmentConfiguracoesBinding binding;


    //Observer do livedata de cargaTotalNecessaria(carga total de horas de atividades complementares necessaria para o usuario entregar)
    private Observer<Integer> cargaTotalNecessaria = new Observer<Integer>() {
        @Override
        public void onChanged(Integer cargaTotalNecessaria) {
            mViewModel.cargaNecessariaInicial = cargaTotalNecessaria;
            setarCargaTotalNaEditText(cargaTotalNecessaria);

        }
    };


    //observer que observa se o email para troca de senha foi enviado
    Observer<String> resultadoEmailRecuperacao = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeRecuperacaoDeSenha){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeRecuperacaoDeSenha = false;

            }

        }
    } ;

    Observer<String> resultadoLogout = new Observer<String>(){

        @Override
        public void onChanged(String s) {
            if(Objects.equals(s, "deslogou")){
                Navigation.findNavController(requireView()).navigate(R.id.fragmentLogin);
            }

        }
    };


    //observer que observa o resultado do save do novoEmail(novo email escolhido na configuração)
    Observer<String> resultadoSaveNovoEmail = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeSaveDeNovoEmail){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeSaveDeNovoEmail = false;

            }else {
                showSnackbar("ocorreu um erro ao tentar sair do app");
            }

        }
    };


    //observer que observa o resultado do save da nova carga horaria total
    Observer<String> resultadoSaveNovaCargaHoraria = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarSnackDeSaveDeNovaCargaHoraria){
                showSnackbar(s);
                mViewModel.podeMostrarSnackDeSaveDeNovaCargaHoraria = false;

            }
        }
    };



    //para explicacao sobre esse metodo visite o comentario do onCreateView do FragmentBoasVindas

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentConfiguracoesViewModel.class);

        binding = FragmentConfiguracoesBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    //para explicacao sobre esse metodo visite o comentario do onViewCreated do FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //inicia os observers
        initObservers();
        //pega carga horaria total inicial
        pegarCargaTotal();
        //pega o email que ja esta cadastrado
        pegarEmail();
        onClickListeners();

    }


    private void onClickListeners() {
        binding.btnMudarSenha.setOnClickListener(v->{
            //chamar a viewModel para enviar o email de troca de senha assim que o botão mudar senha for pressionado
            mViewModel.enviarEmailDeRecuperacaoDeSenha();

        });

        binding.btnSalvarNovoEmail.setOnClickListener(v->{
            //se o email não for igual ao antigo mostra o dialog de reautenticação
            //o dialog de reatuenticação é necessario pq o firebase requer que
            //o usuario se reautentique para confirmar a troca do email
            String email = binding.etNovoEmail.getText().toString();
            if(!email.equals(mViewModel.emailInicial) ){
                mostrarDialogDeReautenticacao(email);
            }


        });

        binding.btnSalvarNovaCargaHoraria.setOnClickListener(v->{
            //quando pressionado salva a nova carga horaria(se ela for diferente da que já estava cadastrada)
            Integer novaCargaHoraria = Integer.valueOf(binding.etCargaHoraria.getText().toString());
            if(!novaCargaHoraria.equals(mViewModel.cargaNecessariaInicial)){
                mViewModel.alterarCargaHorariaTotalDoUser(novaCargaHoraria);

            }


        });

        binding.btnLogout.setOnClickListener(v->{
            mViewModel.deslogar();
        });
    }

    private void mostrarDialogDeReautenticacao(String emailNovo) {
        //aqui eu crio as views das edit texts que servirão para o usuario se autenticar
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_reautenticacao, null);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etSenha = view.findViewById(R.id.etSenha);

        etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        //Construção do dialog, que vai consistir em uma caixa com dois campos para entrada do email e senha de autenticação
        //e um botao para confirmar a autenticação

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
        mViewModel.resultadoLogout.observe(requireActivity(), resultadoLogout);

    }

    //mostra uma mensagem na tela quando necessario
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

    //seta o valor atual da carga total na edittext
    private void setarCargaTotalNaEditText(Integer cargaTotalNecessaria) {
        binding.etCargaHoraria.setText(String.valueOf(cargaTotalNecessaria));
    }

    //seta o email atual na edittext
    private void setarEmailNaEditText(String email){
        binding.etNovoEmail.setText(email);

    }




    //quando esse fragment for destruido remover os observers
    //para garantir que o live data nao continue sendo observado
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.cargaTotalNecessaria.removeObserver(cargaTotalNecessaria);
        mViewModel.resultadoEnvioEmailRecuperacao.removeObserver(resultadoEmailRecuperacao);
        mViewModel.resultadoSaveNovoEmail.removeObserver(resultadoSaveNovoEmail);
        mViewModel.resultadoSaveNovaCargaHoraria.removeObserver(resultadoSaveNovaCargaHoraria);
        mViewModel.resultadoLogout.removeObserver(resultadoLogout);

    }
}
package com.example.atividadescomplementares.telas.boasVindas;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atividadescomplementares.R;
import com.example.atividadescomplementares.databinding.FragmentBoasVindasBinding;
import com.example.atividadescomplementares.telas.boasVindas.viewmodel.FragmentBoasVindasViewModel;


/**
 * Fragment da tela de boas vindas
 */
public class FragmentBoasVindas extends Fragment {

    //instancia da viewModel que é usada como ponte entre os dados e a tela
    private FragmentBoasVindasViewModel mViewModel;

    //objeto binding que serve para acessar as views do arquivo xml
    private FragmentBoasVindasBinding binding;


    //metodo do ciclo de vida do fragment
    //onCreateView é usado para desenhar as views na tela, semelhante ao que acontece no metodo on create da activity
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //init da viewModel
        mViewModel = new ViewModelProvider(this).get(FragmentBoasVindasViewModel.class);

        //init do binding desse fragment
        binding = FragmentBoasVindasBinding.inflate(inflater, container, false);
        //metodo para mudar algumas cores dependendo se o device estiver no modo claro ou escuro
        mudarCorESSeTemaFroClaro();
        return binding.getRoot();
    }

    private void mudarCorESSeTemaFroClaro() {
        //pega qual modo esta ativado no device(claro ou escuro)
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //se for o modo claro, muda a cor do botao de login, se for escuro a cor permanece conforme o definido no xml
        if(currentNightMode != Configuration.UI_MODE_NIGHT_YES){
           binding.getRoot().setBackgroundColor(Color.parseColor("#facfce"));
           binding.loginButton.setBackgroundTintList(requireContext().getResources().getColorStateList(R.color.white));



        }
    }


    //metodo do ciclo de vida do fragment, semelhante ao onStart. Aqui a view ja foi desenhada na tela
    //e operações mais pesadas podem acontecer
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCLickListeners();
    }


    //onclick listeners de algumas views
    private void onCLickListeners() {
        binding.btnCadastrar.setOnClickListener(v->{
            //aqui o navigationComponent é utilizado para navegar para a tela de cadastro assim que o botão de cadastro for clicado
            Navigation.findNavController(v).navigate(FragmentBoasVindasDirections.actionFragmentBoasVindasToFragmentCadastro());
        });
        binding.loginButton.setOnClickListener(v->{
            //aqui o navigationComponent é utilizado para navegar para a tela de login assim que o botão de login for clicado
            Navigation.findNavController(v).navigate(FragmentBoasVindasDirections.actionFragmentBoasVindasToFragmentLogin());

        });
    }
}
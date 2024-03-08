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

public class FragmentBoasVindas extends Fragment {

    private FragmentBoasVindasViewModel mViewModel;
    private FragmentBoasVindasBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentBoasVindasViewModel.class);

        binding = FragmentBoasVindasBinding.inflate(inflater, container, false);
        mudarCorESSeTemaFroClaro();
        return binding.getRoot();
    }

    private void mudarCorESSeTemaFroClaro() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if(currentNightMode != Configuration.UI_MODE_NIGHT_YES){
           binding.getRoot().setBackgroundColor(Color.parseColor("#facfce"));
           binding.loginButton.setBackgroundTintList(requireContext().getResources().getColorStateList(R.color.white));



        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCLickListeners();
    }

    private void onCLickListeners() {
        binding.btnCadastrar.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(FragmentBoasVindasDirections.actionFragmentBoasVindasToFragmentCadastro());
        });
        binding.loginButton.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(FragmentBoasVindasDirections.actionFragmentBoasVindasToFragmentLogin());

        });
    }
}
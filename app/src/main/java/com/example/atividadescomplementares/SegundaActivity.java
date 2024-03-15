package com.example.atividadescomplementares;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.atividadescomplementares.databinding.ActivitySegundaBinding;

public class SegundaActivity extends AppCompatActivity {

    private ActivitySegundaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySegundaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    @Override
    protected void onStop() {
        super.onStop();




    }
}
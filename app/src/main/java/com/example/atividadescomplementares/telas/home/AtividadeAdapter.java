package com.example.atividadescomplementares.telas.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.databinding.ItemModalidadeBinding;

import java.util.ArrayList;
import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

    private List<AtividadeComplementar> listaDeAtividades;

    public AtividadeAdapter(List<AtividadeComplementar> listaDeAtividades) {
        this.listaDeAtividades = listaDeAtividades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemModalidadeBinding binding = ItemModalidadeBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listaDeAtividades.get(position));

    }

    @Override
    public int getItemCount() {
        return listaDeAtividades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemModalidadeBinding binding;
        public ViewHolder(@NonNull ItemModalidadeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        private void bind(AtividadeComplementar atividadeComplementar){

                binding.nomeModalidade.setText(atividadeComplementar.modalidade);
                binding.descricao.setText(atividadeComplementar.descricaoAtividade);
                binding.local.setText(atividadeComplementar.local);
                binding.cargaModalidade.setText(atividadeComplementar.cargaHoraria+"h");





        }
    }
}

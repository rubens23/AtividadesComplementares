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


/**
 * adapter para a recyclerview de atividades complementares
 *
 * recyclerview é o nome da view que vai no xml e essa view serve para mostrar listas na tela.
 *
 * Esse adapter é necessario para pegar cada item da lista e mostrar na tela, reciclando por novos itens
 * assim que a lista for scrollada para cima ou para baixo.
 *
 * Para mais informações sobre os adapters e as recycler views, leia a documentação do android
 */
public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

    //lista que sera mostrada na tela
    private List<AtividadeComplementar> listaDeAtividades;

    public AtividadeAdapter(List<AtividadeComplementar> listaDeAtividades) {
        this.listaDeAtividades = listaDeAtividades;
    }


    //esse metodo infla o layout de cada item no item da recycler view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemModalidadeBinding binding = ItemModalidadeBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    //pega o item da lista e manda para a classe de viewHolder desenhar o item na tela
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listaDeAtividades.get(position));

    }


    //pega a contagem de itens para fazer o controle do scroll e reciclagens de itens
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

        //esse metodo coloca os itens da lista no item que vai na recycler view
        @SuppressLint("SetTextI18n")
        private void bind(AtividadeComplementar atividadeComplementar){

                binding.nomeModalidade.setText(atividadeComplementar.modalidade);
                binding.descricao.setText(atividadeComplementar.descricaoAtividade);
                binding.local.setText(atividadeComplementar.local);
                binding.cargaModalidade.setText(atividadeComplementar.cargaHoraria+"h");





        }
    }
}

package com.example.atividadescomplementares.dados.user;

import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;

import java.util.ArrayList;
import java.util.List;


/**
 * classe que representa as informações do usuario
 */
public class UserInfo {
    public String nome;
    public String email;
    public int cargaHorariaTotal;

    public List<AtividadeComplementar> atividadesComplementares;

    public UserInfo(){

    }

    public UserInfo(String nome, String email, int cargaHorariaTotal, List<AtividadeComplementar> atividadesComplementares) {
        this.nome = nome;
        this.email = email;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.atividadesComplementares = atividadesComplementares;
    }
}

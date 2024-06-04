package com.example.atividadescomplementares.dados.atividadeComplementar;


/**
 * Classe que representa as caracteristicas de uma atividade complementar
 */
public class AtividadeComplementar {
    public String modalidade;
    public String atividade;
    public int cargaHoraria;
    public String descricaoAtividade;
    public String local;

    public String idAtividade;

    public AtividadeComplementar(){

    }

    public AtividadeComplementar(String modalidade, String atividade, int cargaHoraria, String descricaoAtividade, String local, String idAtividade) {
        this.modalidade = modalidade;
        this.atividade = atividade;
        this.cargaHoraria = cargaHoraria;
        this.descricaoAtividade = descricaoAtividade;
        this.local = local;
        this.idAtividade = idAtividade;
    }
}

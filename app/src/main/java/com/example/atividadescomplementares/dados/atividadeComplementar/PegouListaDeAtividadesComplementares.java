package com.example.atividadescomplementares.dados.atividadeComplementar;

import java.util.List;

/**
 * callback que é executado quando uma lista é obtida do firebase.
 */
public interface PegouListaDeAtividadesComplementares {
    void pegouLista(List<AtividadeComplementar> lista);
}

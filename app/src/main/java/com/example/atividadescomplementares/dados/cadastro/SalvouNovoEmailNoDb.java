package com.example.atividadescomplementares.dados.cadastro;


/**
 * callback para avisar quando o novo email é salvo no realtime database do firebase
 */
public interface SalvouNovoEmailNoDb {
    void salvouNovoEmailNoDb(boolean salvou);
}

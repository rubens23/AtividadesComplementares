package com.example.atividadescomplementares.dados.cadastro;


/**
 * callback para avisar quando o cadastro foi realizado
 */
public interface CadastroNoDBCallback {
    void onCadastrouUserNoFirebase(boolean salvouInfoDoUser);
}

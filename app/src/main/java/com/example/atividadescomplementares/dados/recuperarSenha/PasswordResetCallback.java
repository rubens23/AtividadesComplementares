package com.example.atividadescomplementares.dados.recuperarSenha;


/**
 * callback que é chamado quando o email de mudança de senha é enviado
 */
public interface PasswordResetCallback {
    void onEmailEnviado(boolean enviouEmail);
}

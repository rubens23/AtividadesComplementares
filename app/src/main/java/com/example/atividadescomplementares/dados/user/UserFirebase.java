package com.example.atividadescomplementares.dados.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * classe para acessar informações do user no firebase auth
 */
public class UserFirebase {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //metodo para pegar o email cadastrado do user no firebase auth
    public String pegarEmailDoUser(){
        if(user != null){
            return user.getEmail();
        }else {
            return "user não está logado!";
        }
    }
}

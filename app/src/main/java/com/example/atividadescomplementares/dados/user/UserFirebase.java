package com.example.atividadescomplementares.dados.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFirebase {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public String pegarEmailDoUser(){
        if(user != null){
            return user.getEmail();
        }else {
            return "user não está logado!";
        }
    }
}

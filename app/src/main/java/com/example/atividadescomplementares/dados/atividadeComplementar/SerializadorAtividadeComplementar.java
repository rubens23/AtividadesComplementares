package com.example.atividadescomplementares.dados.atividadeComplementar;

import com.google.gson.Gson;

public class SerializadorAtividadeComplementar {
    private static final Gson gson = new Gson();

    public static String serializeAtividade(AtividadeComplementar atividade){
        return gson.toJson(atividade);
    }

    public static  AtividadeComplementar deserializeAtividade(String json){
        return gson.fromJson(json, AtividadeComplementar.class);
    }
}

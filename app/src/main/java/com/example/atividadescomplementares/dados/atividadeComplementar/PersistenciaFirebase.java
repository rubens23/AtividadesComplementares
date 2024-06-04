package com.example.atividadescomplementares.dados.atividadeComplementar;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.atividadescomplementares.dados.cadastro.CadastroNoDBCallback;
import com.example.atividadescomplementares.dados.cadastro.MudouEmail;
import com.example.atividadescomplementares.dados.cadastro.SalvouNovoEmailNoDb;
import com.example.atividadescomplementares.dados.user.PegouCargaHorariaTotal;
import com.example.atividadescomplementares.dados.user.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * classe que contem metodos de acesso ao realtimedatabase do firebase
 */
public class PersistenciaFirebase {

    private DatabaseReference mDatabase;

    //tag para fazer log das funções presentes nessa classe
    private String tag = "PersistenciaFirebase";

    //construtor que recebe a inicialização do database que sera utilizado aqui dentro dessa classe
    public PersistenciaFirebase() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    //função para salvar as informações de cadastro do usuario como nome, email e carga horaria
    public void salvarInformacoesDoNovoUsuario(String nome, String email, int cargaHoraria, CadastroNoDBCallback cadastroNoDBCallback) {
        UserInfo user = new UserInfo(nome, email, cargaHoraria, Collections.emptyList());

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        cadastroNoDBCallback.onCadastrouUserNoFirebase(true);

                    } else {
                        cadastroNoDBCallback.onCadastrouUserNoFirebase(false);
                        Log.e(tag, "erro no metodo salvarInformacoesDoNovoUsuario: " + task.getException());

                    }

                }
            });


        }


    }


    //função para salvar uma nova atividade complementar
    public void salvarNovaAtividadeComplementar(AtividadeComplementar atividadeComplementar, SalvouAtividadeCallback salvouAtividadeCallback) {
        Log.d("monitorandoSave", "salvarNovaAtividadeComplementar eu to aqui dentro do método");

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    List<AtividadeComplementar> atividadesComplementares = new ArrayList<>();



                    if(snapshot.exists()){
                        Log.d("monitorandoSave", "salvarNovaAtividadeComplementar snapshot existe");

                        if(snapshot.getValue() instanceof List){
                            Log.d("monitorandoSave", "salvarNovaAtividadeComplementar snapshot é instancia de lista");

                            GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {
                            };
                            List<AtividadeComplementar> listaAtual = snapshot.getValue(genericTypeIndicator);
                            if(listaAtual != null){
                                atividadesComplementares.addAll(listaAtual);

                            }
                            Log.d("monitorandoSave", "salvarNovaAtividadeComplementar adicionei snapshot à lista de atividades complementares");

                            atividadesComplementares.add(atividadeComplementar);


                        }else if(snapshot.getValue() instanceof HashMap){
                            Log.d("monitorandoSave", "salvarNovaAtividadeComplementar snapshot é instancia de hashmap");

                            AtividadeComplementar atividade = snapshot.getValue(AtividadeComplementar.class);
                            if(atividade != null){
                                atividadesComplementares.add(atividade);
                                Log.d("monitorandoSave", "salvarNovaAtividadeComplementar adicionei o snapshot à lista de atividades");

                                atividadesComplementares.add(atividadeComplementar);

                            }else {
                                Log.d("monitorandoSave", "salvarNovaAtividadeComplementar é um hashmap e a atividade é nula");

                            }

                        }
                    }else {
                        //não tem nenhuma atividade salva no firebase
                        atividadesComplementares.add(atividadeComplementar);


                    }








                    userRef.child("atividadesComplementares").setValue(atividadesComplementares).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                salvouAtividadeCallback.salvouNovaAtividade(true);
                            } else {
                                salvouAtividadeCallback.salvouNovaAtividade(false);
                                Log.e(tag, "erro no metodo salvarNovaAtividadeComplementar: " + task.isSuccessful());
                            }

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }


    //função para pegar as atividades complementares que o user já tem cadastradas
    public void pegarAtividadesComplementaresPorUser(PegouListaDeAtividadesComplementares pegouLista) {
        Log.d("FirebaseDebug", "entrei aqui no metodo pegarAtividadesPorUser");

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("FirebaseDebug", "DataSnapshot value aqui no pegarAtividadesComplementaresPorUser: " + snapshot.getValue());

                    List<AtividadeComplementar> listaAtual = new ArrayList<>();
                    if(snapshot.getValue() instanceof List){
                        GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {};
                        listaAtual = snapshot.getValue(genericTypeIndicator);

                    } else if (snapshot.getValue() instanceof HashMap) {
                        AtividadeComplementar atividade = snapshot.getValue(AtividadeComplementar.class);
                        if (atividade != null) {
                            listaAtual.add(atividade);
                        }

                    }


                    if(listaAtual != null){
                        List<AtividadeComplementar> atividadesComplementares = new ArrayList<>(listaAtual);
                        pegouLista.pegouLista(atividadesComplementares);
                    }else{
                        pegouLista.pegouLista(new ArrayList<>());

                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    //função para pegar as atividades complementares do user por modalidade(Ensino, Pesquisa etc)
    public void pegarAtividadesComplementaresPorUserEModalidade(String modalidadeEscolhida, PegouListaDeAtividadesComplementares pegouLista) {
        Log.d("FirebaseDebug", "entrei aqui no metodo que vai pegar a lista de atividades");

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.d("FirebaseDebug", "DataSnapshot value: " + snapshot.getValue());
                    ArrayList<AtividadeComplementar> listaPorModalidade = new ArrayList<>();



                    if(snapshot.exists()){
                        if(snapshot.getValue() instanceof List){
                            GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {};
                            List<AtividadeComplementar> listaAtual = snapshot.getValue(genericTypeIndicator);


                            if(listaAtual != null){
                                List<AtividadeComplementar> atividadesComplementares = new ArrayList<>(listaAtual);
                                for (AtividadeComplementar atividade: atividadesComplementares){
                                    if(atividade != null){
                                        if(Objects.equals(atividade.modalidade, modalidadeEscolhida)){
                                            listaPorModalidade.add(atividade);

                                        }else {
                                            if(modalidadeEscolhida == "todos"){
                                                listaPorModalidade.add(atividade);

                                            }
                                        }
                                    }



                                }
                                pegouLista.pegouLista(listaPorModalidade);
                            }

                        } else if (snapshot.getValue() instanceof HashMap) {
                            AtividadeComplementar atividade = snapshot.getValue(AtividadeComplementar.class);
                            if(atividade != null){
                                if(Objects.equals(atividade.modalidade, modalidadeEscolhida)){
                                    listaPorModalidade.add(atividade);

                                }else {
                                    if(modalidadeEscolhida == "todos"){
                                        listaPorModalidade.add(atividade);

                                    }
                                }
                            }





                        }
                    }





                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }


    //função para pegar a carga total de horas que o user precisa entregar
    public void pegarCargaTotalNecessaria(PegouCargaHorariaTotal pegouCargaHorariaTotal) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("cargaHorariaTotal").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer cargaHorariaTotal = snapshot.getValue(Integer.class);

                    pegouCargaHorariaTotal.pegouCargaTotal(cargaHorariaTotal);





                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }



    //função para mudar o email cadastrado do user la no authentication do firebase
    public void mudarEmailDoUserNoFirebaseAuth(String email, String senha, String emailNovo, MudouEmail mudouEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        AuthCredential credential = EmailAuthProvider
                .getCredential(email, senha);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //autenticou, agora o email ja pode ser alterado!
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null){
                                user.verifyBeforeUpdateEmail(emailNovo)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    mudouEmail.mudouEmail(true);
                                                }else{
                                                    mudouEmail.mudouEmail(false);
                                                    Log.e(tag, "erro no metodo mudarEmailDoUser: "+task.getException());
                                                }
                                            }
                                        });

                            }
                        }else{
                            mudouEmail.mudouEmail(false);
                            Log.e(tag, "erro no metodo mudarEmailDoUser: "+task.getException());

                        }
                    }
                });
    }

    //função que salva o email alterado no database do user
    public void alterarEmailNoRealtimeDb(String emailNovo) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("email")
                   .setValue(emailNovo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {


                                Log.e(tag, "erro no alterarEmailNoRealtimeDb: " + task.isSuccessful());
                            }


                        }


                    });





        }

    }


    //função para alterar a quantidade de horas necessarias que o user precisa entregar
    public void alterarCargaHorariaTotalDoUser(Integer novaCargaHoraria, SalvouNovaCargaHoraria salvouNovaCargaHoraria) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("cargaHorariaTotal")
                    .setValue(novaCargaHoraria).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                salvouNovaCargaHoraria.salvouNovaCargaHoraria(true);


                            }else{
                                salvouNovaCargaHoraria.salvouNovaCargaHoraria(false);
                                Log.e(tag, "erro no alterarEmailNoRealtimeDb: " + task.isSuccessful());

                            }


                        }


                    });





        }
    }



    public void alterarAtividadeComplementar(AtividadeComplementar atividadeComplementarAtualizada, AtualizouAtividadeComplementar atualizouAtividadeComplementar){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid != null){
            DatabaseReference atividadesComplementaresRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("atividadesComplementares");

            atividadesComplementaresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AtividadeComplementar atividade = snapshot.getValue(AtividadeComplementar.class);
                        if (atividade != null && atividade.idAtividade.equals(atividadeComplementarAtualizada.idAtividade)) {
                            String key = snapshot.getKey();
                            if (key != null) {
                                atividadesComplementaresRef.child(key).setValue(atividadeComplementarAtualizada).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        atualizouAtividadeComplementar.atualizouAtividadeComplementar(true);
                                    } else {
                                        atualizouAtividadeComplementar.atualizouAtividadeComplementar(false);
                                    }
                                });
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        atualizouAtividadeComplementar.atualizouAtividadeComplementar(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    atualizouAtividadeComplementar.atualizouAtividadeComplementar(false);
                }
            });
        }
    }


    public void excluirAtividadeComplementar(AtividadeComplementar atividadeComplementar, ExcluiuAtividadeComplementar excluiuAtividadeComplementar) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid != null) {
            DatabaseReference atividadesComplementaresRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("atividadesComplementares");

            atividadesComplementaresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AtividadeComplementar atividade = snapshot.getValue(AtividadeComplementar.class);
                        if (atividade != null && atividade.idAtividade.equals(atividadeComplementar.idAtividade)) {
                            String key = snapshot.getKey();
                            if (key != null) {
                                atividadesComplementaresRef.child(key).removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        excluiuAtividadeComplementar.excluiuAtividadeComplementar(true);
                                    } else {
                                        excluiuAtividadeComplementar.excluiuAtividadeComplementar(false);
                                    }
                                });
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        excluiuAtividadeComplementar.excluiuAtividadeComplementar(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    excluiuAtividadeComplementar.excluiuAtividadeComplementar(false);
                }
            });
        }
    }
}

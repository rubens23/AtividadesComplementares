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
import java.util.List;
import java.util.Objects;

public class PersistenciaFirebase {

    private DatabaseReference mDatabase;
    private String tag = "PersistenciaFirebase";

    public PersistenciaFirebase() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

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


    public void salvarNovaAtividadeComplementar(AtividadeComplementar atividadeComplementar, SalvouAtividadeCallback salvouAtividadeCallback) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {
                    };
                    List<AtividadeComplementar> listaAtual = snapshot.getValue(genericTypeIndicator);

                    List<AtividadeComplementar> atividadesComplementares;

                    if(listaAtual != null){
                        atividadesComplementares = new ArrayList<>(listaAtual);

                    }else{
                        atividadesComplementares = new ArrayList<>();

                    }



                    atividadesComplementares.add(atividadeComplementar);

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


    public void pegarAtividadesComplementaresPorUser(PegouListaDeAtividadesComplementares pegouLista) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {};
                    List<AtividadeComplementar> listaAtual = snapshot.getValue(genericTypeIndicator);


                    if(listaAtual != null){
                        List<AtividadeComplementar> atividadesComplementares = new ArrayList<>(listaAtual);
                        pegouLista.pegouLista(atividadesComplementares);
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    public void pegarAtividadesComplementaresPorUserEModalidade(String modalidadeEscolhida, PegouListaDeAtividadesComplementares pegouLista) {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("atividadesComplementares").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<List<AtividadeComplementar>> genericTypeIndicator = new GenericTypeIndicator<List<AtividadeComplementar>>() {};
                    List<AtividadeComplementar> listaAtual = snapshot.getValue(genericTypeIndicator);
                    ArrayList<AtividadeComplementar> listaPorModalidade = new ArrayList<>();


                    if(listaAtual != null){
                        List<AtividadeComplementar> atividadesComplementares = new ArrayList<>(listaAtual);
                        for (AtividadeComplementar atividade: atividadesComplementares){
                            if(Objects.equals(atividade.modalidade, modalidadeEscolhida)){
                                listaPorModalidade.add(atividade);

                            }else {
                                if(modalidadeEscolhida == "todos"){
                                    listaPorModalidade.add(atividade);

                                }
                            }
                        }
                        pegouLista.pegouLista(listaPorModalidade);
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

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
}

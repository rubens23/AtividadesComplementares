package com.example.atividadescomplementares.telas.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.atividadescomplementares.dados.atividadeComplementar.AtividadeComplementar;
import com.example.atividadescomplementares.dados.atividadeComplementar.SerializadorAtividadeComplementar;
import com.example.atividadescomplementares.databinding.FragmentHomeBinding;
import com.example.atividadescomplementares.telas.home.interfaces.ClickEditarAtividade;
import com.example.atividadescomplementares.telas.home.viewmodel.FragmentHomeViewModel;
import com.example.atividadescomplementares.telas.login.FragmentLoginDirections;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements ClickEditarAtividade {


    //instancia da viewModel
    private FragmentHomeViewModel mViewModel;

    //instancia do binding
    private FragmentHomeBinding binding;

    private AtividadeAdapter adapter;


    private ItemTouchHelper itemTouchHelper;


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mostrarAlertDialogParaConfirmarAExclusao(viewHolder, direction);

        }
    };




    //fazer o codigo que mostre a quantidade de horas obtidas por modalidade. O total de horas obtidas só será mostrado quando o user escolher a categoria todas
    //observer que observa o resultado da lista de atividades complementares
    Observer<List<AtividadeComplementar>> resultadoListaDeAtividades = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            fazerOCalculoDoTotalDeHorasRestantes(atividadeComplementars);
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };


    //observer que observa o resultado da lista de atividade complementares por modalidade
    Observer<List<AtividadeComplementar>> resultadoListaDeAtividadesPorModalidade = new Observer<List<AtividadeComplementar>>() {
        @Override
        public void onChanged(List<AtividadeComplementar> atividadeComplementars) {
            fazerOCalculoDoTotalDeHorasRestantes(atividadeComplementars);
            colocarAtividadesNaRecyclerView(atividadeComplementars);

        }

    };


    //observer que observa o resultado da carga total
    Observer<String> resultadoCargaTotal = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotal) {
            setarCargaTotalNaView(cargaTotal);


        }

    };



    //observer que observa o resultado da carga horaria obtida até o momento(quantas horas o user já completou)
    Observer<String> resultadoCargaTotalObtida = new Observer<String>() {
        @Override
        public void onChanged(String cargaTotalObtida) {
            setarCargaTotalObtidaNaView(cargaTotalObtida);



        }

    };


    //observer que observa a porcentagem total de horas que servirá para preencher o gráfico
    Observer<Integer> porcentagemTotalDeHoras = new Observer<Integer>() {
        @Override
        public void onChanged(Integer porcentagem) {
            setPorcentagemNoCircularProgressIndicator(porcentagem);



        }

    };

    //ao excluir uma atividade atualizar o grafico levando em consideracao categoria escolhida .
    //todo resolver o bug: excluir atividade complementar > ir para tela de formulário > e voltar para home(isso esta crashando o aplicativo)

    //Observer para observar a exclusão da atividade complementar
    Observer<String> exclusaoAtividadeComplementar = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(mViewModel.podeMostrarResultSnackbar){
                if(s.equals("excluiu")){
                    fazerOCalculoDoTotalDeHorasRestantes(adapter.listaDeAtividades);
                    showSnackbar("Atividade complementar excluída com sucesso!");
                }else{
                    showSnackbar("Houve uma falha na exclusão da atividade");

                }
                mViewModel.podeMostrarResultSnackbar = false;

            }

        }
    };


    /*
    observação sobre os observers acima:

    cada observer desse vai receber um valor assim que ele for chamado. O resultado de algo é o valor recebido la da viewModel.

    O fluxo é mais ou menos o seguinte:
    fragment -> chama viewModel para pegar dados -> viewModel chama os metodos do firebase p/ pegar os dados -> dados são obtidos -> viewModel é avisada atraves de callbacks -> viewModel coloca o valor no liveData -> fragment que estava observando pega o novo valor do liveData
     */



    //para explicacao sobre esse metodo visite o comentario do onCreateView do FragmentBoasVindas

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FragmentHomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    //para explicacao sobre esse metodo visite o comentario do onViewCreated do FragmentBoasVindas

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObservers();
        mViewModel.pegarListaDeAtividades();
        fazerSelecaoInicialDoChip();
        onClickListeners();
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvAtividadesComplementares);

    }

    private void fazerSelecaoInicialDoChip() {
        binding.chipTodos.setChecked(true);
        binding.containerChipGroup.post(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.containerChipGroup.smoothScrollTo(binding.chipTodos.getLeft(), binding.chipTodos.getTop());
                    }
                }
        );
    }

    //preenche o grafico de acordo com a porcentagem de horas concluidas
    private void setPorcentagemNoCircularProgressIndicator(Integer porcentagem) {
        int progresso = Math.min(100, porcentagem);


        binding.indicadorCircularDeModalidade.setProgress(progresso);
    }

    private void mostrarAlertDialogParaConfirmarAExclusao(RecyclerView.ViewHolder viewHolder, int direction) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());

        //titulo e mensagem do alertDialog
        alertDialogBuilder.setTitle("Confirmar Exclusão");
        alertDialogBuilder.setMessage("Você tem certeza que deseja excluir essa atividade?");

        //Definir um botão positivo que confirma a exclusão
        alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excluirAtividade(viewHolder);
            }
        });

        //Definir um botão negativo que cancela a exclusão
        alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void excluirAtividade(RecyclerView.ViewHolder viewHolder) {
        AtividadeComplementar atividadeComplementar = adapter.listaDeAtividades.get(viewHolder.getPosition());
        excluirAtividadeDaPersistencia(atividadeComplementar);
        adapter.listaDeAtividades.remove(viewHolder.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

    }

    private void excluirAtividadeDaPersistencia(AtividadeComplementar atividadeComplementar) {
        Log.d("monitorandoExclusao", "to aqui no metodo excluir atividade");
        mViewModel.excluirAtividadeComplementar(atividadeComplementar);
    }


    private void showSnackbar(String s) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();


    }

    //seta uma das textviews que fica dentro do grafico

    private void setarCargaTotalObtidaNaView(String cargaTotalObtida) {
        Integer validNumber = mViewModel.tryParseInt(cargaTotalObtida);
        if(validNumber != null){
            boolean jaAtingiuTotalDeCargaHoraria = mViewModel.checarSeJaAtingiuTotalDeCargaHoraria(validNumber);
            if(!jaAtingiuTotalDeCargaHoraria){
                binding.cargaTotalObtida.setText(cargaTotalObtida);
                mViewModel.completouHorasComplementares = false;
                showCompletouGrafico(false);
            }else{

                if(mViewModel.mCargaTotal > 0){
                    binding.cargaTotalObtida.setText(mViewModel.mCargaTotal.toString());
                    mViewModel.completouHorasComplementares = true;
                    showCompletouGrafico(true);
                }
            }

        }

    }

    //seta uma das textviews que fica dentro do grafico

    @SuppressLint("SetTextI18n")
    private void setarCargaTotalNaView(String cargaTotal) {
        binding.cargaTotal.setText("/"+cargaTotal+"h");
    }


    //pega o calculo de quantas horas faltam para o user completar as atividades complementares
    private void fazerOCalculoDoTotalDeHorasRestantes(List<AtividadeComplementar> atividadeComplementars) {
        mViewModel.calcularHorasRestantes(atividadeComplementars);
    }


    //coloca as atividades complementares na recycler view
    private void colocarAtividadesNaRecyclerView(List<AtividadeComplementar> atividadeComplementars) {
        ArrayList<AtividadeComplementar> listaAdapter = new ArrayList<>();
        for (AtividadeComplementar atividade : atividadeComplementars){
            if(atividade != null){
                listaAdapter.add(atividade);
            }
        }
        adapter = new AtividadeAdapter(listaAdapter, this);
        binding.rvAtividadesComplementares.setAdapter(adapter);
    }



    private void initObservers() {
        mViewModel.listaDeAtividades.observe(requireActivity(), resultadoListaDeAtividades);
        mViewModel.cargaTotalLD.observe(requireActivity(), resultadoCargaTotal);
        mViewModel.cargaTotalObtida.observe(requireActivity(), resultadoCargaTotalObtida);
        mViewModel.porcentagemHorasConcluidas.observe(requireActivity(), porcentagemTotalDeHoras);
        mViewModel.listaDeAtividadesPorModalidade.observe(requireActivity(), resultadoListaDeAtividadesPorModalidade);
        mViewModel.resultadoExclusaoAtividadeComplementar.observe(requireActivity(), exclusaoAtividadeComplementar);
    }

    private void onClickListeners() {
        binding.btnMostrarChips.setOnClickListener(v->{
            //controla a visibilidade dos Chips
            if(mViewModel.showChips){
                binding.containerChipGroup.setVisibility(View.GONE);
                mViewModel.showChips = false;
                animarFiltroParaBaixo(binding.btnMostrarChips);

            }else {
                binding.containerChipGroup.setVisibility(View.VISIBLE);
                mViewModel.showChips = true;
                animarFiltroParaCima(binding.btnMostrarChips);


            }

        });


        //controla a visibilidade do grafico
        binding.btnMostrarGrafico.setOnClickListener(v->{
            if(mViewModel.showGrafico){
                binding.containerTextoGrafico.setVisibility(View.GONE);
                binding.indicadorCircularDeModalidade.setVisibility(View.GONE);
                binding.labelGrafico.setVisibility(View.INVISIBLE);
                mViewModel.showGrafico = false;
                animarSetaGraficoParaBaixo(binding.btnMostrarGrafico);
                showCompletouGrafico(false);

            }else{
                binding.containerTextoGrafico.setVisibility(View.VISIBLE);
                binding.indicadorCircularDeModalidade.setVisibility(View.VISIBLE);
                binding.labelGrafico.setVisibility(View.VISIBLE);
                mViewModel.showGrafico = true;
                animarSetaGraficoParaCima(binding.btnMostrarGrafico);
                showCompletouGrafico(true);
            }
        });


        //listener para cada Chip. Quando clicar no chip, a lista de atividades filtrada por modalidade será exibida
        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                if(checkedIds.size() == 0){
                    mViewModel.pegarListaDeAtividadesPorModalidade("todos");
                    mudarTextViewCategoria("TOTAL");



                }else {
                    if(checkedIds.get(0) == binding.chipEnsino.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("ensino");
                        mudarTextViewCategoria("Ensino");

                    }
                    if(checkedIds.get(0) == binding.chipPesquisa.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("pesquisa");
                        mudarTextViewCategoria("Pesquisa");


                    }
                    if(checkedIds.get(0) == binding.chipExtensao.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("extensao");
                        mudarTextViewCategoria("Extensão");


                    }
                    if(checkedIds.get(0) == binding.chipEsporteArteCultura.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("esporte");
                        mudarTextViewCategoria("Esporte");


                    }
                    if(checkedIds.get(0) == binding.chipCidadania.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("cidadania");
                        mudarTextViewCategoria("Cidadania");


                    }
                    if(checkedIds.get(0) == binding.chipTodos.getId()){
                        mViewModel.pegarListaDeAtividadesPorModalidade("todos");
                        mudarTextViewCategoria("TOTAL");


                    }
                }
            }
        });
    }

    private void mudarTextViewCategoria(String str) {
        binding.labelGrafico.setText(str);
    }

    private void showCompletouGrafico(boolean mostrar) {
        if(mViewModel.completouHorasComplementares){
            if(mostrar){
                binding.cvCompletouAtividades.setVisibility(View.VISIBLE);
            }else {
                binding.cvCompletouAtividades.setVisibility(View.GONE);
            }
        }else{
            binding.cvCompletouAtividades.setVisibility(View.GONE);

        }
    }


    /**
     *
     * a seguir alguns metodos para animação da seta de mostrar/esconder grafico/chips
     */
    private void animarSetaGraficoParaCima(ImageButton btnMostrarGrafico) {
        btnMostrarGrafico.animate().rotationBy(180f).setDuration(50).start();

    }

    private void animarSetaGraficoParaBaixo(ImageButton btnMostrarGrafico) {
        btnMostrarGrafico.animate().rotationBy(-180f).setDuration(50).start();


    }

    private void animarFiltroParaCima(ImageButton image){
        image.animate().rotationBy(180f).setDuration(50).start();



    }

    private void animarFiltroParaBaixo(ImageButton image){
        image.animate().rotationBy(-180f).setDuration(50).start();



    }


    //destruir os observers ao destruir o fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel.listaDeAtividades.removeObserver(resultadoListaDeAtividades);
        mViewModel.cargaTotalLD.removeObserver(resultadoCargaTotal);
        mViewModel.cargaTotalObtida.removeObserver(resultadoCargaTotalObtida);
        mViewModel.porcentagemHorasConcluidas.removeObserver(porcentagemTotalDeHoras);
        mViewModel.listaDeAtividadesPorModalidade.removeObserver(resultadoListaDeAtividadesPorModalidade);
        mViewModel.resultadoExclusaoAtividadeComplementar.removeObserver(exclusaoAtividadeComplementar);

    }

    @Override
    public void clickEditarAtividade(AtividadeComplementar atividadeComplementar) {
        String atividadeSerializada = SerializadorAtividadeComplementar.serializeAtividade(atividadeComplementar);
        Navigation.findNavController(requireView()).navigate(FragmentHomeDirections.actionPaginaInicialToFragmentEditarAtividadeComplementar(atividadeSerializada));

    }
}
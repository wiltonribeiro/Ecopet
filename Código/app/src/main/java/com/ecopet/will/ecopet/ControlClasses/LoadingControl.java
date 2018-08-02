package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FactsData;
import com.ecopet.will.ecopet.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by willrcneto on 15/03/18.
 */

public class LoadingControl {

    private TextView outputFact;
    private List<String> listFacts = new ArrayList<>();
    private RelativeLayout loadingLayout;
    private Random random;
    private Activity activity;

    public LoadingControl(Activity activity){
        //adiciona a lista de fatos o array de fatos contidos em FactsData
        this.listFacts.addAll(Arrays.asList(FactsData.getFacts()));
        this.outputFact = activity.findViewById(R.id.outputFacts);
        this.loadingLayout = activity.findViewById(R.id.loading);
        this.activity = activity;
        this.random = new Random();
    }

    public LoadingControl(View view){
        //adiciona a lista de fatos o array de fatos contidos em FactsData
        this.listFacts.addAll(Arrays.asList(FactsData.getFacts()));
        this.outputFact = view.findViewById(R.id.outputFacts);
        this.loadingLayout = view.findViewById(R.id.loading);
        this.random = new Random();
    }

    //inicia o carregamento
    public void startLoading(){
        //seta o texto de fato para vazio por enquanto
        this.outputFact.setText("");

        //gera um numero aleatorio
        int index = random.nextInt(listFacts.size());

        //seta o texto da posicao aleatoria da lista de fatos
        outputFact.setText(listFacts.get(index));
        //deixa visivel a tela
        loadingLayout.setVisibility(View.VISIBLE);
    }

    //inicia o carregamento sem texto
    public void startBlankLoading(){
        this.outputFact.setText("");
        loadingLayout.setVisibility(View.VISIBLE);
    }

    public void finishLoading(){
        loadingLayout.setVisibility(View.GONE);
    }

    public Activity getActivity() {
        return activity;
    }
}

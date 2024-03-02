package com.example.genius;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ActivityJogo extends AppCompatActivity {
    private final static String ARQ_REF = "REF";
    Button btnVerde, btnAmarelo, btnVermelho, btnAzul, btnJogar;
    TextView txtPontuacao, txtErrou;
    List<Integer> sequencia = new ArrayList<>();
    int jogada = 0, tempoClick;
    boolean jogar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);
        btnJogar = findViewById(R.id.btnJogar);
        btnVerde = findViewById(R.id.btnVerde);
        btnAmarelo = findViewById(R.id.btnAmarelo);
        btnVermelho = findViewById(R.id.btnVermelho);
        btnAzul = findViewById(R.id.btnAzul);
        btnVerde.setClickable(false);
        btnAmarelo.setClickable(false);
        btnVermelho.setClickable(false);
        btnAzul.setClickable(false);
        txtPontuacao = findViewById(R.id.txtPontuacao);
        txtErrou = findViewById(R.id.txtErrou);
        getPontuacao();
    }
    public void clickJogar(View view){
        sequencia.clear();
        tempoClick = 0;
        jogar = false;
        jogada = 0;
        startGame();
    }
    private void startGame() {
        if(!jogar){
                Handler inicio = new Handler();
                inicio.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sequenciaCores();
                        ascenderBotao();
                        Handler clicando = new Handler();
                        clicando.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnVerde.setClickable(true);
                                btnAmarelo.setClickable(true);
                                btnVermelho.setClickable(true);
                                btnAzul.setClickable(true);
                            }
                        }, tempoClick);
                    }
                },1000);
            }
    }
    public void sequenciaCores(){ //função que gera a sequencia a ser mostrada
        ArrayList<Integer> cores = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        Random rand = new Random();
        int tam = cores.size();
        int indice = rand.nextInt(tam);
        int elem = cores.get(indice);
        sequencia.add(elem);
    }
    public void ascenderBotao(){ //função que ascende a sequencia que foi gerada usando setPressed
        Handler handler = new Handler();
        btnVerde.setClickable(false);
        btnAmarelo.setClickable(false);
        btnVermelho.setClickable(false);
        btnAzul.setClickable(false);
        btnJogar.setClickable(false);

        for (int i = 0; i < sequencia.size(); i++){
            int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                switch (sequencia.get(finalI)) {
                    case 1:
                        btnVerde.setPressed(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnVerde.setPressed(false);
                            }
                        }, 1000);
                        break;
                    case 2:
                        btnAmarelo.setPressed(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnAmarelo.setPressed(false);
                            }
                        }, 1000);
                        break;
                    case 3:
                        btnVermelho.setPressed(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnVermelho.setPressed(false);
                            }
                        }, 1000);
                        break;
                    case 4:
                        btnAzul.setPressed(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnAzul.setPressed(false);
                            }
                        }, 1000);
                        break;
                    }
                }
            }, 1500*(i+1));
            tempoClick = 1500 * i;
        }
    }
    public void clickVerde(View view){
        validar(1);
    } //valida o click no botão verde
    public void clickAmarelo(View view){
        validar(2);
    } //valida o click no botão amarelo
    public void clickVermelho(View view){
        validar(3);
    } //valida o click no botão vermelho
    public void clickAzul(View view){
        validar(4);
    } //valida o click no botão azul
    public void validar(int id){ //funcão que valida cada click referente as cores da sequencia
        if(!jogar){
            if(sequencia.get(jogada) == id) {
                jogada++;
                if(jogada == sequencia.size()){
                    jogada = 0;
                    Handler inicio = new Handler();
                    inicio.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sequenciaCores();
                            ascenderBotao();
                            Handler clicando = new Handler();
                            clicando.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnVerde.setClickable(true);
                                    btnAmarelo.setClickable(true);
                                    btnVermelho.setClickable(true);
                                    btnAzul.setClickable(true);
                                }
                            }, tempoClick);
                        }
                    },1000);
                }
            }
            else{
                jogada = 0;
                jogar = true;
                btnVerde.setClickable(false);
                btnAmarelo.setClickable(false);
                btnVermelho.setClickable(false);
                btnAzul.setClickable(false);
                btnJogar.setClickable(true);
                setarPontuacao(sequencia.size()-1);
                getPontuacao();
                sequencia.clear();
                Handler errou = new Handler();
                txtErrou.setVisibility(View.VISIBLE);
                errou.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtErrou.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

            }
        }
    }
    public void setarPontuacao(int pontuacao){ //armazena usando sharedPreferences o tamanho da seq.
        SharedPreferences preferences = getSharedPreferences(ARQ_REF, 0);
        int pont_ant = Integer.parseInt(preferences.getString("pontuacao","0"));
        if(pontuacao > pont_ant) {
            String pont = "";
            pont = pontuacao + "";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("pontuacao", pont);
            editor.commit();
        }
    }
    public void getPontuacao(){ //atualiza o txtPontuacao com o recorde do sharedPreferences
        SharedPreferences preferences = getSharedPreferences(ARQ_REF, 0);
        txtPontuacao.setText(preferences.getString("pontuacao","0"));
    }
}
package com.purple.calcettotimer;

import java.util.LinkedList;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CalcettoTimerActivity extends AppCompatActivity {

    int min = 5;
    int sec = 0;

    Cronometro cronometro;

    // INIZIO codice generato automaticamente

    private Button stopButton;
    private Button restartButton;
    private Button reset_button;
    private Button start_button;
    private Button piuButtonMin;
    private EditText editTextMin;
    private Button menoButtonMin;
    private Button piuButtonSec;
    private EditText editTextSec;
    private Button menoButtonSec;
    private Button set_time_button;
    private TextView cronometroTextView;

    private void linkaTuttiWidget() {

        stopButton = findViewById(R.id.stopButton);
        restartButton = findViewById(R.id.restartButton);
        reset_button = findViewById(R.id.reset_button);
        start_button = findViewById(R.id.start_button);
        piuButtonMin = findViewById(R.id.piuButtonMin);
        editTextMin = findViewById(R.id.editTextMin);
        menoButtonMin = findViewById(R.id.menoButtonMin);
        piuButtonSec = findViewById(R.id.piuButtonSec);
        editTextSec = findViewById(R.id.editTextSec);
        menoButtonSec = findViewById(R.id.menoButtonSec);
        set_time_button = findViewById(R.id.set_time_button);
        cronometroTextView = findViewById(R.id.cronometroTextView);

        inserisciViewInLista();
    }


    List<View> listaView;

    private void inserisciViewInLista() {

        listaView = new LinkedList<>();

        listaView.add(stopButton);
        listaView.add(restartButton);
        listaView.add(reset_button);
        listaView.add(start_button);
        listaView.add(piuButtonMin);
        listaView.add(menoButtonMin);
        listaView.add(piuButtonSec);
        listaView.add(menoButtonSec);
        listaView.add(set_time_button);

    }

    // FINE codice generato automaticamente


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        caricaSfondo();

        linkaTuttiWidget();

        trasparenzaBottoni();

        int altSchermo = getResources().getDisplayMetrics().heightPixels;

        if (altSchermo <= 600)
            cronometroTextView.setTextSize(48);

        MediaPlayer mp = MediaPlayer.create(CalcettoTimerActivity.this, R.raw.arbitro);

        cronometro = new Cronometro(cronometroTextView, mp);

        start_button.setOnClickListener(startButtonListener);

        stopButton.setOnClickListener(stopButtonListener);

        reset_button.setOnClickListener(resetButtonListener);

        restartButton.setOnClickListener(restartButtonListener);

        set_time_button.setOnClickListener(setButtonListener);

        piuButtonMin.setOnClickListener(piuMinModifValoriButtonListener);
        menoButtonMin.setOnClickListener(menoMinModifValoriButtonListener);
        piuButtonSec.setOnClickListener(piuSecModifValoriButtonListener);
        menoButtonSec.setOnClickListener(menoSecModifValoriButtonListener);

        cronometro.setCronometro(min, sec);

        aggiornaTextViewPicker();
    }

    private void caricaSfondo() {
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);

        Glide.with(this)
                .load(R.drawable.campo)
                .into(backgroundImageView);
    }

    @Override
    public void onPause() {
        cronometro.resetCronometro();

        super.onPause();
    }

    @Override
    public void onStop() {
        cronometro.resetCronometro();

        super.onStop();
    }

    @Override
    public void onDestroy() {

        cronometro.resetCronometro();

        super.onDestroy();
    }

    private void trasparenzaBottoni() {
        for (View v : listaView) {
            v.getBackground().setAlpha(210);
        }
    }

    private void aggiornaTextViewPicker() {
        editTextMin.setText(min + " min.");
        editTextSec.setText(sec + " sec.");
    }

    private OnClickListener piuSecModifValoriButtonListener = new OnClickListener() {

        public void onClick(View v) {

            sec++;

            if (sec > 99)
                sec = 99;

            aggiornaTextViewPicker();

        }

    };


    private OnClickListener menoSecModifValoriButtonListener = new OnClickListener() {

        public void onClick(View v) {

            sec--;

            if (sec < 0)
                sec = 0;

            aggiornaTextViewPicker();

        }

    };

    private OnClickListener piuMinModifValoriButtonListener = new OnClickListener() {

        public void onClick(View v) {

            min++;

            if (min > 99)
                min = 99;

            aggiornaTextViewPicker();

        }

    };

    private OnClickListener menoMinModifValoriButtonListener = new OnClickListener() {

        public void onClick(View v) {

            min--;

            if (min < 0)
                min = 0;

            aggiornaTextViewPicker();

        }

    };


    private OnClickListener setButtonListener = new OnClickListener() {

        public void onClick(View v) {

            cronometro.setCronometro(min, sec);

        }

    };


    private OnClickListener resetButtonListener = new OnClickListener() {

        public void onClick(View v) {

            cronometro.resetCronometro();

        }

    };

    private OnClickListener startButtonListener = new OnClickListener() {

        public void onClick(View v) {

            cronometro.startCronometro();

        }

    };

    private OnClickListener stopButtonListener = new OnClickListener() {

        public void onClick(View v) {

            cronometro.stopCronometro();

        }

    };


    private OnClickListener restartButtonListener = new OnClickListener() {

        public void onClick(View v) {

            cronometro.restartCronometro();

        }

    };
}
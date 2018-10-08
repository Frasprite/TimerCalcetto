package com.purple.calcettotimer;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

public class CalcettoTimerActivity extends Activity {
    /** Called when the activity is first created. */
	
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

	private void linkaTuttiWidget(){

	    stopButton = (Button) findViewById(R.id.stopButton);
	    restartButton = (Button) findViewById(R.id.restartButton);
	    reset_button = (Button) findViewById(R.id.reset_button);
	    start_button = (Button) findViewById(R.id.start_button);
	    piuButtonMin = (Button) findViewById(R.id.piuButtonMin);
	    editTextMin = (EditText) findViewById(R.id.editTextMin);
	    menoButtonMin = (Button) findViewById(R.id.menoButtonMin);
	    piuButtonSec = (Button) findViewById(R.id.piuButtonSec);
	    editTextSec = (EditText) findViewById(R.id.editTextSec);
	    menoButtonSec = (Button) findViewById(R.id.menoButtonSec);
	    set_time_button = (Button) findViewById(R.id.set_time_button);
	    cronometroTextView = (TextView) findViewById(R.id.cronometroTextView);

	    inserisciViewInLista();
	}



	List<View> listaView;

	private void inserisciViewInLista(){

	    listaView = new LinkedList<View>();

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
        
        linkaTuttiWidget();
        
        trasparenzaBottoni();
        
        
        int altSchermo = getResources().getDisplayMetrics().heightPixels;
        
        if(altSchermo <= 600)
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
    
    @Override
    public void onPause(){
    	
    	//Log.d("GGG", "onPause");
    	
    	cronometro.resetCronometro();
    	
    	
    	super.onPause();
    }

    @Override
    public void onStop(){
    	
    	//Log.d("GGG", "onStop");
    	
    	cronometro.resetCronometro();
    	
    	super.onStop();
    	
    }

    @Override
    public void onDestroy(){
    	
    	//Log.d("GGG", "onDestroy");
    	
    	cronometro.resetCronometro();
    	
    	super.onDestroy();
    	
    }
    
    
    
    private void trasparenzaBottoni(){
    	
    	for(View v: listaView){
    		
    		v.getBackground().setAlpha(210);
    		
    	}
    	
    }
    
    
    private void aggiornaTextViewPicker(){
    	
    	editTextMin.setText(min + " min.");
        editTextSec.setText(sec + " sec.");
    	
    }
    
    
    private OnClickListener piuSecModifValoriButtonListener = new OnClickListener() {
    	
    	public void onClick(View v) {
    		
    		sec ++;
    		
    		if(sec>99)
    			sec=99;
    		
    		aggiornaTextViewPicker();
    		
    	}

    };
    
    
    private OnClickListener menoSecModifValoriButtonListener = new OnClickListener() {
    	
    	public void onClick(View v) {
    		
    		sec --;
    		
    		if(sec<0)
    			sec=0;
    		
    		aggiornaTextViewPicker();
    		
    	}

    };
    
    private OnClickListener piuMinModifValoriButtonListener = new OnClickListener() {
    	
    	public void onClick(View v) {

    		min ++;
    		
    		if(min>99)
    			min=99;
    		
    		aggiornaTextViewPicker();
    		
    	}

    };
    
    private OnClickListener menoMinModifValoriButtonListener = new OnClickListener() {
    	
    	public void onClick(View v) {
    		
    		min --;
    		
    		if(min<0)
    			min=0;
    		
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
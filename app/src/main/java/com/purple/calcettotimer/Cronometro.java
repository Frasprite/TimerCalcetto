package com.purple.calcettotimer;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Cronometro {

	TextView textView;
	
	MyCount mycounter;
	
	MediaPlayer mp;
	
	
	int min;
	int sec;
	
	
	int mintot;
	int sectot;
	
	Boolean started;
	
	public Cronometro(TextView textView, MediaPlayer mp){
		
		this.textView = textView;
		
		this.mp = mp;
		
		min = 0;
		sec = 0;
		mintot = 0;
		sectot = 0;
		
		mycounter = null;
		
		started = false;
		
	}
	
	
	
    
    public void aggiornaStringaCronometro(){
    	
    	String zeromin = "";
    	String zerosec = "";
    	
    	if(min <= 9)
    		zeromin = "0";
    	
    	if(sec <= 9)
    		zerosec = "0";
    	
    	String totale = zeromin + min + ":" + zerosec + sec;
    	
    	textView.setText(totale);
    	
    }
    
    
    public void setCronometro(int min, int sec){
    	
    	stopCronometro();
    	
    	mintot = this.min = min;
    	sectot = this.sec = sec;
    	
    	aggiornaStringaCronometro();
    	
    }
    
    
    public void startCronometro(){
    	
    	if(!started){
    		
	    	started = true;
	    	
	    	mycounter = new MyCount( ((min*60)+sec) * 1000,1000);
	    	
	    	mycounter.start();
    	
    	}
    	
    }
    
    
	public void stopCronometro(){
		
		started = false;
		
		if(mycounter != null)
			mycounter.cancel();
		
		
		mp.stop();
		
	}

	
    
	public void resetCronometro(){
			
		stopCronometro();
		
		min = mintot;
		sec = sectot;
		
		aggiornaStringaCronometro();
			
	}
	
	
	public void restartCronometro(){
		
		resetCronometro();
		
		startCronometro();
		
	}

    
    
    
    public class MyCount extends CountDownTimer{
    	
    	public MyCount(long millisInFuture, long countDownInterval) {
    		
    		super(millisInFuture, countDownInterval);
    	
    	}
    	
    	
    	private void playSuono(){
    		
    		mp.setLooping(true);
    		
    		try {
    			
				mp.prepare();
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		mp.seekTo(0);
    		
    		mp.start();
    		
    	}
    	
    	
    	@Override
    	public void onFinish() {
    		
    		textView.setText("00:00");
    		
    		playSuono();
    		
    	}
    	
    	@Override
    	public void onTick(long millisUntilFinished) {
    		
    		if(sec > 0){
    		
    			sec --;
    			
    		}else{
    			
    			min --;
    			sec = 59;
    			
    		}
    		
        	aggiornaStringaCronometro();
    		
    	}

    }
    
    
	
}

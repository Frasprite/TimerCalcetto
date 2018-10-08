package com.purple.calcettotimer

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main.*
import java.util.*

class TimerActivity : AppCompatActivity(), View.OnClickListener {

    private var min = 5
    private var sec = 0

    private lateinit var listaView: MutableList<View>
    private lateinit var cronometro: Cronometro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        caricaSfondo()
        inserisciViewInLista()
        trasparenzaBottoni()
        dimensioniCronometro()
        initCronometro()
        aggiungiClickListener()
        aggiornaTextViewPicker()
    }

    override fun onClick(view: View) {
        when (view) {
            start_button -> { cronometro.startCronometro() }
            stopButton -> { cronometro.stopCronometro() }
            reset_button -> { cronometro.resetCronometro() }
            restartButton -> { cronometro.restartCronometro() }
            set_time_button -> { cronometro.setCronometro(min, sec) }
            piuButtonMin -> {
                min++

                if (min > 99) min = 99

                aggiornaTextViewPicker()
            }
            menoButtonMin -> {
                min--

                if (min < 0) min = 0

                aggiornaTextViewPicker()
            }
            piuButtonSec -> {
                sec++

                if (sec > 99) sec = 99

                aggiornaTextViewPicker()
            }
            menoButtonSec -> {
                sec--

                if (sec < 0) sec = 0

                aggiornaTextViewPicker()
            }
        }
    }

    private fun caricaSfondo() {
        Glide.with(this)
                .load(R.drawable.campo)
                .into(backgroundImageView)
    }

    private fun inserisciViewInLista() {
        Log.d(TAG, "inserisciViewInLista - start")
        listaView = LinkedList()

        listaView.add(stopButton)
        listaView.add(restartButton)
        listaView.add(reset_button)
        listaView.add(start_button)
        listaView.add(piuButtonMin)
        listaView.add(menoButtonMin)
        listaView.add(piuButtonSec)
        listaView.add(menoButtonSec)
        listaView.add(set_time_button)
    }

    private fun trasparenzaBottoni() {
        Log.d(TAG, "trasparenzaBottoni - start")
        for (v in listaView) {
            v.background.alpha = 210
        }
    }

    private fun dimensioniCronometro() {
        val altSchermo = resources.displayMetrics.heightPixels

        if (altSchermo <= 600)
            cronometroTextView.textSize = 48f
    }

    private fun initCronometro() {
        val mp = MediaPlayer.create(this@TimerActivity, R.raw.arbitro)

        cronometro = Cronometro(cronometroTextView, mp)
        cronometro.setCronometro(min, sec)
    }

    private fun aggiungiClickListener() {
        start_button.setOnClickListener(this)
        stopButton.setOnClickListener(this)
        reset_button.setOnClickListener(this)
        restartButton.setOnClickListener(this)
        set_time_button.setOnClickListener(this)
        piuButtonMin.setOnClickListener(this)
        menoButtonMin.setOnClickListener(this)
        piuButtonSec.setOnClickListener(this)
        menoButtonSec.setOnClickListener(this)
    }

    private fun aggiornaTextViewPicker() {
        editTextMin.setText(getString(R.string.minutes, min))
        editTextSec.setText(getString(R.string.seconds, sec))
    }

    companion object {
        private val TAG = TimerActivity::class.java.simpleName
    }
}
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

    private lateinit var viewList: MutableList<View>
    private lateinit var chronos: Chronos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        loadBackground()
        addViewsOnList()
        addLayerToButtons()
        setTextDimension()
        initChronos()
        initClickListener()
        updateTextViewPicker()
    }

    override fun onClick(view: View) {
        when (view) {
            startButton -> { chronos.startChronos() }
            stopButton -> { chronos.stopChronos() }
            resetButton -> { chronos.resetChronos() }
            restartButton -> { chronos.restartChronos() }
            setTimerButton -> { chronos.setChronos(min, sec) }
            plusButtonMin -> {
                min++

                if (min > 99) min = 99

                updateTextViewPicker()
            }
            minusButtonMin -> {
                min--

                if (min < 0) min = 0

                updateTextViewPicker()
            }
            plusButtonSec -> {
                sec++

                if (sec > 99) sec = 99

                updateTextViewPicker()
            }
            minusButtonSec -> {
                sec--

                if (sec < 0) sec = 0

                updateTextViewPicker()
            }
        }
    }

    private fun loadBackground() {
        Glide.with(this)
                .load(R.drawable.campo)
                .into(backgroundImageView)
    }

    private fun addViewsOnList() {
        Log.d(TAG, "addViewsOnList - start")
        viewList = LinkedList()

        viewList.add(stopButton)
        viewList.add(restartButton)
        viewList.add(resetButton)
        viewList.add(startButton)
        viewList.add(plusButtonMin)
        viewList.add(minusButtonMin)
        viewList.add(plusButtonSec)
        viewList.add(minusButtonSec)
        viewList.add(setTimerButton)
    }

    private fun addLayerToButtons() {
        Log.d(TAG, "addLayerToButtons - start")
        for (v in viewList) {
            v.background.alpha = 210
        }
    }

    private fun setTextDimension() {
        val altSchermo = resources.displayMetrics.heightPixels

        if (altSchermo <= 600)
            timerTextView.textSize = 48f
    }

    private fun initChronos() {
        val mp = MediaPlayer.create(this@TimerActivity, R.raw.arbitro)

        chronos = Chronos(timerTextView, mp)
        chronos.setChronos(min, sec)
    }

    private fun initClickListener() {
        startButton.setOnClickListener(this)
        stopButton.setOnClickListener(this)
        resetButton.setOnClickListener(this)
        restartButton.setOnClickListener(this)
        setTimerButton.setOnClickListener(this)
        plusButtonMin.setOnClickListener(this)
        minusButtonMin.setOnClickListener(this)
        plusButtonSec.setOnClickListener(this)
        minusButtonSec.setOnClickListener(this)
    }

    private fun updateTextViewPicker() {
        editTextMin.setText(getString(R.string.minutes, min))
        editTextSec.setText(getString(R.string.seconds, sec))
    }

    companion object {
        private val TAG = TimerActivity::class.java.simpleName
    }
}
package com.purple.calcettotimer

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.widget.TextView
import java.io.IOException

class Chronos(internal var textView: TextView, internal var mp: MediaPlayer) {

    private var myCounter: MyCount? = null

    internal var min: Int = 0
    internal var sec: Int = 0

    private var minutes: Int = 0
    private var seconds: Int = 0

    private var started: Boolean? = null

    init {
        min = 0
        sec = 0
        minutes = 0
        seconds = 0

        myCounter = null

        started = false
    }

    fun updateTimerText() {
        var zeromin = ""
        var zerosec = ""

        if (min <= 9)
            zeromin = "0"

        if (sec <= 9)
            zerosec = "0"

        val total = "$zeromin$min:$zerosec$sec"

        textView.text = total
    }

    fun setChronos(min: Int, sec: Int) {

        stopChronos()

        this.min = min
        minutes = this.min
        this.sec = sec
        seconds = this.sec

        updateTimerText()
    }

    fun startChronos() {
        if ((!started!!)) {

            started = true

            myCounter = MyCount(((min * 60 + sec) * 1000).toLong(), 1000)

            myCounter!!.start()

        }
    }

    fun stopChronos() {
        started = false

        if (myCounter != null)
            myCounter!!.cancel()

        mp.stop()
    }

    fun resetChronos() {
        stopChronos()

        min = minutes
        sec = seconds

        updateTimerText()
    }

    fun restartChronos() {
        resetChronos()
        startChronos()
    }

    inner class MyCount(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        private fun playSound() {
            mp.isLooping = true

            try {
                mp.prepare()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mp.seekTo(0)
            mp.start()
        }

        override fun onFinish() {
            textView.text = textView.context.getText(R.string.timer_zero)

            playSound()
        }

        override fun onTick(millisUntilFinished: Long) {
            if (sec > 0) {
                sec--
            } else {
                min--
                sec = 59
            }

            updateTimerText()
        }
    }
}

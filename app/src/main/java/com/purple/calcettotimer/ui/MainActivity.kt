package com.purple.calcettotimer.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.purple.calcettotimer.R
import com.purple.calcettotimer.model.FootballMatch
import com.purple.calcettotimer.model.TimerState
import com.purple.calcettotimer.util.NotificationUtil
import com.purple.calcettotimer.util.PrefUtil
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_config.*
import java.util.*


/**
 * Main and unique activity of our app.
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
                                            NumberPicker.OnValueChangeListener {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

        val logTag: String = MainActivity::class.java.simpleName
    }

    private lateinit var timer: CountDownTimer

    private val match = FootballMatch(5, 2, 60, 0,
            0, 0, TimerState.Stopped)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        bottomMenu.setOnNavigationItemSelectedListener(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()

        initPickers()
        initTimer()

        removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()

        if (match.timerState == TimerState.Running) {
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, match.secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        } else if (match.timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(this)
        }

        PrefUtil.setPreviousTimerLengthSeconds(match.timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(match.secondsRemaining, this)
        PrefUtil.setTimerState(match.timerState, this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.start -> {
                startTimer()
                match.timerState =  TimerState.Running
                updateButtons()
            }
            R.id.pause -> {
                timer.cancel()
                match.timerState = TimerState.Paused
                updateButtons()
            }
            R.id.stop -> {
                timer.cancel()
                onTimerFinished()
                updateButtons()
            }
        }

        return true
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        Log.v(logTag, String.format(Locale.getDefault(), "oldVal: %d, newVal: %d", oldVal, newVal))

        when (picker!!) {
            playerPicker -> match.numberOfPlayers = newVal
            turnOnCagePicker -> match.turnOnCage = newVal
            minutesPicker -> match.minutes = newVal
            secondsPicker -> match.seconds = newVal
        }

        // Function to calculate time on cage
        // Minutes of game / (N. players * Turn on cage)
        // Sample : 60 min / (5 * 2) = 6 minutes per turn

        val timer = ((match.minutes * 60) + match.seconds) / (match.numberOfPlayers * match.turnOnCage)
        Log.v(logTag, "onValueChange - Setting timer to $timer")
        PrefUtil.setTimerLength(timer, this)
        initTimer()
    }

    /**
     * Set on value changed listener to pickers.
     */
    private fun initPickers() {
        playerPicker.setOnValueChangedListener(this@MainActivity)
        turnOnCagePicker.setOnValueChangedListener(this@MainActivity)
        minutesPicker.setOnValueChangedListener(this@MainActivity)
        secondsPicker.setOnValueChangedListener(this@MainActivity)
    }

    /**
     * Loading previous timer or initialize it if empty.
     */
    private fun initTimer() {
        match.timerState = PrefUtil.getTimerState(this)

        // Don't change the length of the timer which is already running
        if (match.timerState == TimerState.Stopped) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        match.secondsRemaining = if (match.timerState == TimerState.Running || match.timerState == TimerState.Paused) {
            PrefUtil.getSecondsRemaining(this)
        } else {
            match.timerLengthSeconds
        }

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0) {
            match.secondsRemaining -= nowSeconds - alarmSetTime
        }

        if (match.secondsRemaining <= 0) {
            onTimerFinished()
        } else if (match.timerState == TimerState.Running) {
            startTimer()
        }

        // Updating UI
        updateButtons()
        updateCountdownUI()
    }

    /**
     * Update UI and timer status when it is finished.
     */
    private fun onTimerFinished() {
        match.timerState = TimerState.Stopped

        // Set default length of the timer or init new one
        setNewTimerLength()

        //progressCountdown.progress = 0

        PrefUtil.setSecondsRemaining(match.timerLengthSeconds, this)
        match.secondsRemaining = match.timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    /**
     * Start a new timer for the match.
     */
    private fun startTimer() {
        match.timerState = TimerState.Running

        timer = object : CountDownTimer(match.secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                match.secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInSeconds = PrefUtil.getTimerLength(this)
        match.timerLengthSeconds = (lengthInSeconds * 1L)
        //progressCountdown.max = match.timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        match.timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        //progressCountdown.max = match.timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        textViewCountdown.text = String.format("%02d:%02d", (match.secondsRemaining % 3600) / 60, (match.secondsRemaining % 60))
        //progressCountdown.progress = (match.timerLengthSeconds - match.secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (match.timerState) {
            TimerState.Running -> {
                Log.v(logTag, "updateButtons - Timer is running")

                bottomMenu.menu.findItem(R.id.start).isEnabled = false
                bottomMenu.menu.findItem(R.id.pause).isEnabled = true
                bottomMenu.menu.findItem(R.id.stop).isEnabled = true
                playerPicker.isEnabled = false
                turnOnCagePicker.isEnabled = false
                minutesPicker.isEnabled = false
                secondsPicker.isEnabled = false
            }
            TimerState.Stopped -> {
                Log.v(logTag, "updateButtons - Timer is stopped")

                bottomMenu.menu.findItem(R.id.start).isEnabled = true
                bottomMenu.menu.findItem(R.id.pause).isEnabled = false
                bottomMenu.menu.findItem(R.id.stop).isEnabled = false
                playerPicker.isEnabled = true
                turnOnCagePicker.isEnabled = true
                minutesPicker.isEnabled = true
                secondsPicker.isEnabled = true
            }
            TimerState.Paused -> {
                Log.v(logTag, "updateButtons - Timer is paused")

                bottomMenu.menu.findItem(R.id.start).isEnabled = true
                bottomMenu.menu.findItem(R.id.pause).isEnabled = false
                bottomMenu.menu.findItem(R.id.stop).isEnabled = true
                playerPicker.isEnabled = false
                turnOnCagePicker.isEnabled = false
                minutesPicker.isEnabled = false
                secondsPicker.isEnabled = false
            }
        }
    }
}

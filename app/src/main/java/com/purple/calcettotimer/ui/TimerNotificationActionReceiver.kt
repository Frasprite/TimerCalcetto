package com.purple.calcettotimer.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.purple.calcettotimer.model.TimerState
import com.purple.calcettotimer.util.NotificationUtil
import com.purple.calcettotimer.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                MainActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = MainActivity.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                MainActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val secondsRemaining = PrefUtil.getTimerLength(context) * 1L
                val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}

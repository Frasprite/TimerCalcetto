package com.purple.calcettotimer.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.purple.calcettotimer.model.TimerState
import com.purple.calcettotimer.util.NotificationUtil
import com.purple.calcettotimer.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}

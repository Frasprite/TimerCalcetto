package com.purple.calcettotimer.model

data class FootballMatch(var numberOfPlayers: Int = 5, var turnOnCage: Int = 2,
                         var minutes: Int = 60, var seconds: Int = 0,
                         var timerLengthSeconds: Long = 0, var secondsRemaining: Long = 0,
                         var timerState: TimerState = TimerState.Stopped)
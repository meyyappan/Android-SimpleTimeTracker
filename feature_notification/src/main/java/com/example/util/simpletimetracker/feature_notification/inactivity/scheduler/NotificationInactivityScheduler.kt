package com.example.util.simpletimetracker.feature_notification.inactivity.scheduler

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.util.simpletimetracker.domain.di.AppContext
import com.example.util.simpletimetracker.feature_notification.recevier.NotificationReceiver
import javax.inject.Inject

class NotificationInactivityScheduler @Inject constructor(
    @AppContext private val context: Context
) {

    private val alarmManager
        get() = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun schedule(durationMillis: Long) {
        val timestamp = System.currentTimeMillis() + durationMillis

        scheduleAtTime(timestamp)
    }

    fun cancelSchedule() {
        alarmManager?.cancel(getPendingIntent())
    }

    private fun scheduleAtTime(timestamp: Long) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager?.setExactAndAllowWhileIdle(RTC_WAKEUP, timestamp, getPendingIntent())
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                alarmManager?.setExact(RTC_WAKEUP, timestamp, getPendingIntent())
            }
            else -> {
                alarmManager?.set(RTC_WAKEUP, timestamp, getPendingIntent())
            }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_INACTIVITY_REMINDER
        }

        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
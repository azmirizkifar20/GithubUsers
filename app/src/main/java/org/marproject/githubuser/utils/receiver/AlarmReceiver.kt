package org.marproject.githubuser.utils.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import org.marproject.githubuser.R
import org.marproject.githubuser.view.splash.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val CHANNEL_ID = "Channel_1"
        const val CHANNEL_NAME = "AlarmManager channel"

        private const val ID_REPEATING = 100
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = "Github user"

        if (message != null) {
            showNotification(context, title, message, ID_REPEATING)
        }
    }

    fun setRepeatingAlarm(context: Context, message: String) {
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun showNotification(context: Context, title: String, message: String, notifId: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManager = context.getSystemService<NotificationManager>()
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager?.notify(notifId, notification)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager?.cancel(pendingIntent)
    }
}
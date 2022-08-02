package com.jerimkaura.contacts.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.jerimkaura.contacts.MainActivity
import com.jerimkaura.contacts.data.Contact


class AlarmReceiver :
    BroadcastReceiver() {
    private lateinit var mNotificationManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {

        val contact = intent.getStringExtra("contact")
        val contacts = intent.getSerializableExtra("CONTACTS") as List<Contact?>
        contacts.forEach {
            if (it != null) {
                sendSMS(it.phoneNumber, "Hi there ${it.name}")
            }
        }
        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotification(context, contact)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNotification(context: Context, contact: String?) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Contact")
            .setContentText("Messages sent to all contacts. Tap to open and view saved contacts.")
            .setSmallIcon(com.jerimkaura.contacts.R.drawable.logo2)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        sendSMS(contact, "Hi there $contact")
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun sendSMS(phoneNo: String?, msg: String?) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        const val CHANNEL_ID = "channel ID"
        const val NOTIFICATION_ID = 0
    }

}
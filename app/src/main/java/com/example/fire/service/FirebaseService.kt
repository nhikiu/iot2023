package com.example.fire.service

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.fire.R
import com.example.fire.SENSOR
import com.example.fire.ui.component.splash.SplashActivity
import com.google.firebase.database.*

import android.os.VibrationEffect
import android.os.Vibrator

class FirebaseService : Service() {

    private val TAG = "FirebaseService"
    private val CHANNEL_ID = "ForegroundServiceChannel"

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        databaseReference = FirebaseDatabase.getInstance().getReference(SENSOR)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hệ thống báo cháy")
            .setContentText("Thông báo lắng nghe trạng thái cháy")
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(1, notification)

        // Listen for changes on Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as? Map<*, *>

                if (dataMap != null) {
                    val buzzerValue = dataMap["buzzer"] as? Boolean ?: false
                    val ledValue = dataMap["led"] as? Boolean ?: false

                    if (buzzerValue && ledValue) {
                        Log.d(TAG, "Received true value(s) from Firebase")
                        sendNotification()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error reading from Firebase", databaseError.toException())
            }
        })

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun sendNotification() {
        val notificationIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )

        val soundUri = Uri.parse("android.resource://$packageName/${R.raw.fire_alarm}")

        val defaultSoundUri = soundUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Phát hiện lửa")
            .setContentText("Còi và đèn cảnh báo đang kêu")
            .setSound(defaultSoundUri)
            .setLights(R.color.blue, 300, 100)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_iot)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}

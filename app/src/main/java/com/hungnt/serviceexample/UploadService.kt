package com.hungnt.serviceexample

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UploadService : Service() {

    private val CHANNEL_ID = "UPLOADING"

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
        Log.d("Upload", "Start service")
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Upload", "On start command")
        Single.just(1)
            .map {
                Log.d("Upload", "Uploading.....1")
            }
            .delay(2, TimeUnit.SECONDS)
            .map {
                Log.d("Upload", "Uploading.....2")
            }
            .delay(2, TimeUnit.SECONDS)
            .map {
                Log.d("Upload", "Uploading.....3")
            }
            .delay(2, TimeUnit.SECONDS)
            .subscribe ({
                Log.d("Upload", "Upload finish, stop service")
                stopForeground(true)
                stopSelf()
            }, {

            })
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Upload", "Destroy service")
    }

    fun createNotification() {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Uploading")
            .setTicker("Uploading")
            .setSound(null)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .build()
//        manager?.notify(122, notification)
        startForeground(122, notification)
    }

    fun cancelNotification() {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        manager?.cancel(122)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create a Notification channel
        val name = "Uploading service"
        val descriptionText = "Uploading image"
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        mChannel.setSound(null, null)
        mChannel.setShowBadge(false)
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        manager?.createNotificationChannel(mChannel)
    }
}
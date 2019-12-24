package com.hungnt.serviceexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit


class UploadImageWorker(appContext: Context, workerParams: WorkerParameters) :
    RxWorker(appContext, workerParams) {

    private val CHANNEL_ID = "UPLOADING"

    override fun createWork(): Single<Result> {
        return Flowable.just(1, 2, 3, 4, 5)
            .doOnNext {
                Log.d("Test", "Uploading image")
                createNotification()
            }
            .delay(3, TimeUnit.SECONDS)
            .toList()
            .map {
                Result.success()
            }
            .doOnSuccess {
                cancelNotification()
            }
            .onErrorReturn {
                Result.retry()
            }
    }

    fun createNotification() {
        val manager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
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
        manager?.notify(122, notification)
    }

    fun cancelNotification() {
        val manager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
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
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        manager?.createNotificationChannel(mChannel)
    }

}
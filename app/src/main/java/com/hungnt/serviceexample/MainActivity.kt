package com.hungnt.serviceexample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStart.setOnClickListener {
//                        startUploadingImageTask()
            val intent = Intent(this, UploadService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

    private fun startUploadingImageTask() {
        val uploadRequest = OneTimeWorkRequestBuilder<UploadImageWorker>()
            .build()
        WorkManager.getInstance(this.applicationContext).enqueue(uploadRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    Log.d("State", workInfo.state.toString())
                }
            })
    }
}

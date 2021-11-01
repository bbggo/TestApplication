package com.bbgo.myapplication.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder

import android.app.PendingIntent

import android.app.NotificationManager

import android.app.NotificationChannel
import android.graphics.Color
import android.net.Uri
import android.os.Build
import com.bbgo.myapplication.R


/**
 * @Description:
 * @Author: wangyuebin
 * @Date: 2021/10/11 10:33 上午
 */
class TestService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val CHANNEL_ONE_ID = "CHANNEL_ONE_ID"
        val CHANNEL_ONE_NAME = "CHANNEL_ONE_ID"
        var notificationChannel: NotificationChannel? = null
        val builder: Notification.Builder = Notification.Builder(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
            builder.setChannelId(CHANNEL_ONE_ID)
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jianshu.com/p/14ba95c6c3e2"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notification: Notification = builder
            .setTicker("Nature")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("这是一个测试标题")
            .setContentIntent(pendingIntent)
            .setContentText("这是一个测试内容")
            .build()
//        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        return notification
    }
}
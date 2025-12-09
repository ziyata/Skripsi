package com.example.skripsi.core.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skripsi.R

object StockNotification {

    private const val CHANNEL_ID = "stock_low_channel"
    private const val CHANNEL_NAME = "Peringatan Stok"
    private const val CHANNEL_DESC = "Notifikasi stok barang hampir habis"

    fun showLowStock(context: Context, barangNama: String, stok: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }
            nm.createNotificationChannel(channel)
        }

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Stok hampir habis")
            .setContentText("$barangNama tersisa $stok item")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // pakai hashCode nama sebagai id supaya unique per barang
        nm.notify(barangNama.hashCode(), notif)
    }
}

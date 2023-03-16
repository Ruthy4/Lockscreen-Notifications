package com.ify.lockscreennotification

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationButton = findViewById<Button>(R.id.grant_notification)
        notificationButton.setOnClickListener {
//            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
//            startActivity(intent)

//            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
//            startActivity(intent)

            checkListenerServicePermission()
        }
    }

    @SuppressLint("InlinedApi")
    fun getIntentNotificationListenerSettings(): Intent {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
        } else
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", this.packageName)
        intent.putExtra("app_uid", this.applicationInfo.uid)

        return intent

    }

    private fun isNotificationListenerServiceAllowed(): Boolean {
        val cn = ComponentName(this, AppNotificationListenerService::class.java)
        val flat = Settings.Secure.getString(
            this.contentResolver,
            "enabled_notification_listeners"
        )
        return flat?.contains(cn.flattenToString()) == true
    }

    /*
     * Checks if access to device's notifications is allowed.
     * If not, show system settings page, where the permission
     * can be granted.
     */
    private fun checkListenerServicePermission() {
        val allowed = isNotificationListenerServiceAllowed()
        if (!allowed) {
            startActivity(getIntentNotificationListenerSettings())
        }
    }
}

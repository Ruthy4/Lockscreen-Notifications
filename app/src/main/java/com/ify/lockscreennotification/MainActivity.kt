package com.ify.lockscreennotification

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationButton = findViewById<Button>(R.id.grant_notification)
        notificationButton.setOnClickListener {
            checkListenerServicePermission()
        }
    }

    @SuppressLint("InlinedApi", "ServiceCast")
    private fun getIntentForNotificationAccess(
        packageName: String,
        notificationAccessServiceClassName: String
    ): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS)
                .putExtra(
                    Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                    ComponentName(packageName, notificationAccessServiceClassName).flattenToString()
                )
        }
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        val value = "$packageName/$notificationAccessServiceClassName"
        val key = ":settings:fragment_args_key"
        intent.putExtra(key, value)
        intent.putExtra(":settings:show_fragment_args", Bundle().also { it.putString(key, value) })
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
            startActivity(
                getIntentForNotificationAccess(
                    "com.ify.lockscreennotification",
                    AppNotificationListenerService::class.java.name
                )
            )
        }
    }
}

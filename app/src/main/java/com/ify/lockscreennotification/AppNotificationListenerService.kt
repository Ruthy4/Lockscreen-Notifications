package com.ify.lockscreennotification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class AppNotificationListenerService : NotificationListenerService() {

    companion object {
        // List of active notifications on the device.
        // Can be accessed from the outside classes.
        val list = mutableListOf<StatusBarNotification>()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        updateNotificationList()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        updateNotificationList()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        updateNotificationList()
    }

    private fun updateNotificationList() {
        list.clear()
        list.addAll(activeNotifications)
    }
}
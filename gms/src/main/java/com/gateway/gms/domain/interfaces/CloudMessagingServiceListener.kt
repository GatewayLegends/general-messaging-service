package com.gateway.gms.domain.interfaces

import com.google.firebase.messaging.RemoteMessage as GoogleRemoteMessage
import com.huawei.hms.push.RemoteMessage as HuaweiRemoteMessage

interface CloudMessagingServiceListener {
    fun onMessageReceived(message: GoogleRemoteMessage)
    fun onMessageReceived(message: HuaweiRemoteMessage?)

    fun onNewToken(token: String)
}
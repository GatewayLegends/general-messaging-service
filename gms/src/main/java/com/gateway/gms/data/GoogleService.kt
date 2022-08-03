package com.gateway.gms.data

import com.gateway.gms.domain.CloudMessaging
import com.google.firebase.messaging.FirebaseMessaging

class GoogleService(private val messaging: FirebaseMessaging) : CloudMessaging {
    override fun subscribeToTopic(topic: String) =
        safeTaskCall { messaging.subscribeToTopic(topic) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { messaging.unsubscribeFromTopic(topic) }

    override fun getToken() = safeTaskCall { messaging.token }

    override fun deleteToken() = safeTaskCall { messaging.deleteToken() }
}
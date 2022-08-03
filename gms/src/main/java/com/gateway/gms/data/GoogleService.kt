package com.gateway.gms.data

import com.gateway.gms.domain.CloudMessaging
import com.gateway.gms.domain.MessagingTask
import com.google.firebase.messaging.FirebaseMessaging

class GoogleService(private val messaging: FirebaseMessaging) : CloudMessaging {
    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.subscribeToTopic(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.unsubscribeFromTopic(topic)) }

    override fun getToken() = safeTaskCall { MessagingTask.Google(messaging.token) }

    override fun deleteToken() = safeTaskCall { MessagingTask.Google(messaging.deleteToken()) }
}
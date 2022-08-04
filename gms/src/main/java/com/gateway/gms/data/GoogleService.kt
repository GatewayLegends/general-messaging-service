package com.gateway.gms.data

import com.gateway.gms.di.GMServiceLocator
import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.models.MessagingTask
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

open class GoogleService : CloudMessaging, FirebaseMessagingService() {
    private val messaging: FirebaseMessaging by lazy { GMServiceLocator.googleService }

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.subscribeToTopic(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.unsubscribeFromTopic(topic)) }

    override fun getToken() = safeTaskCall { MessagingTask.Google(messaging.token) }

    override fun deleteToken() = safeTaskCall { MessagingTask.Google(messaging.deleteToken()) }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d(message.toString())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d(token)

    }
}
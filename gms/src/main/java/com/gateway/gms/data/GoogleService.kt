package com.gateway.gms.data

import com.gateway.gms.di.GMServiceLocator
import com.gateway.gms.domain.interfaces.CloudMessagingService
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.gateway.gms.domain.models.MessagingTask
import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.models.ServiceFailure
import com.gateway.gms.utils.Constants
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class GoogleService : CloudMessagingService, FirebaseMessagingService() {
    private val messaging: FirebaseMessaging by lazy { GMServiceLocator.googleService }
    override val listener: CloudMessagingServiceListener? by lazy { GMServiceLocator.listener }

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.subscribeToTopic(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.unsubscribeFromTopic(topic)) }

    override fun getToken(): Resource<String> {
        GMServiceLocator.token?.let {
            return@getToken Resource.Success(data = it)
        }
        return Resource.Fail(error = ServiceFailure.NoTokenError())
    }

    override fun deleteToken() = safeTaskCall { MessagingTask.Google(messaging.deleteToken()) }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        listener?.onMessageReceived(message = message)
        Timber.d(message.toString())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        listener?.onNewToken(token = token)
        Timber.d(token)
        with(GMServiceLocator.sharedPref.edit()){
            putString(Constants.SharedPref.TOKEN, token)
        }
    }
}
package com.gateway.gms.data

import android.content.SharedPreferences
import com.gateway.gms.domain.interfaces.CloudMessagingService
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.gateway.gms.domain.models.MessagingTask
import com.gateway.gms.utils.Constants
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GoogleService : CloudMessagingService, FirebaseMessagingService() {
    lateinit var messaging: FirebaseMessaging
    private val listener: CloudMessagingServiceListener?
        get() = GMSConfigurations.listener

    private val sharedPreferences: SharedPreferences?
        get() = GMSConfigurations.sharedPreferences

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.subscribeToTopic(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Google(messaging.unsubscribeFromTopic(topic)) }

    override fun getToken(): String? =
        sharedPreferences?.getString(Constants.SharedPref.TOKEN, null)

    override fun deleteToken() = safeTaskCall { MessagingTask.Google(messaging.deleteToken()) }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        listener?.onMessageReceived(message = message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        listener?.onNewToken(token = token)
        sharedPreferences?.edit()?.run {
            putString(Constants.SharedPref.TOKEN, token)
            apply()
        }
    }
}

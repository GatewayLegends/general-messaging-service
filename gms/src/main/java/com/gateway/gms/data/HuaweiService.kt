package com.gateway.gms.data

import android.content.SharedPreferences
import com.gateway.gms.domain.interfaces.CloudMessagingService
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.gateway.gms.domain.models.MessagingTask
import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.models.ServiceFailure
import com.gateway.gms.utils.Constants
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage

class HuaweiService : CloudMessagingService, HmsMessageService() {
    lateinit var messaging: HmsMessaging

    private val listener: CloudMessagingServiceListener?
        get() = GMSConfigurations.listener

    private val sharedPreferences: SharedPreferences?
        get() = GMSConfigurations.sharedPreferences

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.subscribe(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.unsubscribe(topic)) }

    override fun getToken(): String? =
        sharedPreferences?.getString(Constants.SharedPref.TOKEN, null)

    override fun deleteToken(): Resource<Void> =
        Resource.Fail(error = ServiceFailure.FeatureNotProvidedByService())

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        listener?.onMessageReceived(message = p0)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        listener?.onNewToken(token = p0.toString())
        sharedPreferences?.edit()?.run {
            putString(Constants.SharedPref.TOKEN, p0)
            apply()
        }
    }
}

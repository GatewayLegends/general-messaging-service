package com.gateway.gms.data

import com.gateway.gms.di.GMServiceLocator
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
    private val messaging: HmsMessaging by lazy { GMServiceLocator.huaweiService }
    override val listener: CloudMessagingServiceListener? by lazy { GMServiceLocator.listener }

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.subscribe(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.unsubscribe(topic)) }

    override fun getToken(): Resource<String> {
        GMServiceLocator.token?.let {
            return@getToken Resource.Success(data = it)
        }
        return Resource.Fail(error = ServiceFailure.NoTokenError())
    }

    override fun deleteToken(): Resource<Void> =
        Resource.Fail(error = ServiceFailure.FeatureNotProvidedByService())

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        listener?.onMessageReceived(message = p0)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        listener?.onNewToken(token = p0.toString())
        with(GMServiceLocator.sharedPref.edit()) {
            putString(Constants.SharedPref.TOKEN, p0)
        }
    }
}
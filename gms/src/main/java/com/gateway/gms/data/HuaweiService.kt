package com.gateway.gms.data

import com.gateway.gms.di.GMServiceLocator
import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.models.MessagingTask
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage

open class HuaweiService : CloudMessaging, HmsMessageService() {
    private val messaging: HmsMessaging by lazy { GMServiceLocator.huaweiService }

    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.subscribe(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.unsubscribe(topic)) }

    override fun getToken() = safeTaskCall { MessagingTask.Huawei<Nothing>(null) }

    override fun deleteToken() = safeTaskCall { MessagingTask.Huawei<Nothing>(null) }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
    }
}
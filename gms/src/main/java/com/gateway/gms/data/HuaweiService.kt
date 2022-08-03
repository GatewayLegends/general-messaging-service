package com.gateway.gms.data

import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.models.MessagingTask
import com.huawei.hms.push.HmsMessaging

class HuaweiService(private val messaging: HmsMessaging) : CloudMessaging {
    override fun subscribeToTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.subscribe(topic)) }

    override fun unsubscribeFromTopic(topic: String) =
        safeTaskCall { MessagingTask.Huawei(messaging.unsubscribe(topic)) }

    override fun getToken() = safeTaskCall { MessagingTask.Huawei<Nothing>(null) }

    override fun deleteToken() = safeTaskCall { MessagingTask.Huawei<Nothing>(null) }
}
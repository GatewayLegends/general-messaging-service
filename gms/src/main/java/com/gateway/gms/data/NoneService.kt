package com.gateway.gms.data

import com.gateway.gms.domain.interfaces.CloudMessagingService
import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.models.ServiceFailure


class NoneService : CloudMessagingService {
    override fun subscribeToTopic(topic: String): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

    override fun unsubscribeFromTopic(topic: String): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

    override fun getToken(): String? = null

    override fun deleteToken(): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

}

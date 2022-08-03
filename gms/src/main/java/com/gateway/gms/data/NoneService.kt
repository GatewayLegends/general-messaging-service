package com.gateway.gms.data

import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.models.ServiceFailure


class NoneService : CloudMessaging {
    override fun subscribeToTopic(topic: String): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

    override fun unsubscribeFromTopic(topic: String): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

    override fun getToken(): Resource<String> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())

    override fun deleteToken(): Resource<Void> =
        Resource.Fail(error = ServiceFailure.ServiceNotDetected())
}
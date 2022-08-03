package com.gateway.gms.data

import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CloudMessagingRepositoryImpl(private val service: CloudMessaging) : CloudMessagingRepository {
    override fun subscribeToTopic(topic: String): Flow<Resource<Void>> =
        wrapWithFlow { service.subscribeToTopic(topic = topic) }

    override fun unsubscribeFromTopic(topic: String): Flow<Resource<Void>> =
        wrapWithFlow { service.unsubscribeFromTopic(topic = topic) }

    override fun getToken(): Flow<Resource<String>> = wrapWithFlow(service::getToken)

    override fun deleteToken(): Flow<Resource<Void>> = wrapWithFlow(service::deleteToken)

    private fun <T> wrapWithFlow(block: () -> Resource<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        emit(block())
    }
}
package com.gateway.gms.domain.interfaces

import com.gateway.gms.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface CloudMessagingRepository {
    fun subscribeToTopic(topic: String): Flow<Resource<Void>>
    fun unsubscribeFromTopic(topic: String): Flow<Resource<Void>>
    fun getToken(): Flow<Resource<String>>
    fun deleteToken(): Flow<Resource<Void>>
}
package com.gateway.gms.domain

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import timber.log.Timber

interface CloudMessaging {
    fun subscribeToTopic(topic: String) : Resource<Void>
    fun unsubscribeFromTopic(topic: String) : Resource<Void>
    fun getToken(): Resource<String>
    fun deleteToken() : Resource<Void>

    fun <T> safeTaskCall(block: () -> Task<T>) = catchTaskError(block)

    private fun <T> catchTaskError(block: () -> Task<T>) =
        try {
            Resource.Success(data = Tasks.await(block()))
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Fail(data = e.message)
        }
}
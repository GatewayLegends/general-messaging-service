package com.gateway.gms.domain

import timber.log.Timber
import com.google.android.gms.tasks.Tasks as GoogleTasks
import com.huawei.hmf.tasks.Tasks as HuaweiTasks

interface CloudMessaging {
    fun subscribeToTopic(topic: String): Resource<Void>
    fun unsubscribeFromTopic(topic: String): Resource<Void>
    fun getToken(): Resource<String>
    fun deleteToken(): Resource<Void>

    fun <T> safeTaskCall(block: () -> MessagingTask<T>) = catchTaskError(block())

    private fun <T> catchTaskError(block: MessagingTask<T>) =
        try {
            val result = when (block) {
                is MessagingTask.Google -> GoogleTasks.await(block.task)
                is MessagingTask.Huawei -> HuaweiTasks.await(block.task)
            }
            Resource.Success(data = result)
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Fail(data = e.message)
        }
}
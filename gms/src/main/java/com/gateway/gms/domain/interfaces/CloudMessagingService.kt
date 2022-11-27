package com.gateway.gms.domain.interfaces

import com.gateway.gms.domain.models.MessagingTask
import com.gateway.gms.domain.models.Resource
import com.gateway.gms.domain.models.ServiceFailure
import timber.log.Timber
import com.google.android.gms.tasks.Tasks as GoogleTasks
import com.huawei.hmf.tasks.Tasks as HuaweiTasks

interface CloudMessagingService {
    fun subscribeToTopic(topic: String): Resource<Void>
    fun unsubscribeFromTopic(topic: String): Resource<Void>
    fun getToken(): String?
    fun deleteToken(): Resource<Void>

    var listener: CloudMessagingServiceListener?

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
            Resource.Fail(error = ServiceFailure.UnknownError(message = e.message))
        }
}

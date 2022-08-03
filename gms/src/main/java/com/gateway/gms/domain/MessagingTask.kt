package com.gateway.gms.domain

import com.google.android.gms.tasks.Task as GoogleTask
import com.huawei.hmf.tasks.Task as HuaweiTask

sealed class MessagingTask<out T> {
    data class Google<T>(val task: GoogleTask<T>) : MessagingTask<T>()
    data class Huawei<T>(val task: HuaweiTask<T>?) : MessagingTask<T>()
}

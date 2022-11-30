package com.gateway.gms

import android.content.Context
import com.gateway.gms.data.GMSConfigurations
import com.gateway.gms.di.GMSInitializer
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener

class GMSManager(
    context: Context,
    listener: CloudMessagingServiceListener? = null
) : CloudMessagingRepository {

    private val gms: GMSInitializer = GMSInitializer(context, listener)

    fun setListener(listener: CloudMessagingServiceListener? = null) {
        GMSConfigurations.listener = listener
    }

    override fun subscribeToTopic(topic: String) =
        gms.repository.subscribeToTopic(topic)

    override fun unsubscribeFromTopic(topic: String) =
        gms.repository.unsubscribeFromTopic(topic)

    override val token
        get() = gms.repository.token

    override fun deleteToken() = gms.repository.deleteToken()
}

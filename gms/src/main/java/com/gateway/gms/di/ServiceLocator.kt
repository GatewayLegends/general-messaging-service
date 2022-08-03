package com.gateway.gms.di

import android.app.Application
import com.gateway.gms.data.GoogleService
import com.gateway.gms.data.HuaweiService
import com.gateway.gms.domain.CloudMessaging
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.push.HmsMessaging

object ServiceLocator {
    @Volatile
    lateinit var application: Application
    val googleMessagingService: CloudMessaging by lazy { provideGoogleMessagingService() }
    val huaweiMessagingService: CloudMessaging by lazy { provideHuaweiMessagingService() }

    /**
     * This function should called in MainActivity

     * to provide an application instance for the service.
     *
     * @author Ahmed Mones
     */
    fun initializeService(application: Application) {
        if (this::application.isInitialized.not()) {
            synchronized(application) { this.application = application }
        }
    }

    private fun provideGoogleMessagingService() =
        GoogleService(messaging = FirebaseMessaging.getInstance())

    private fun provideHuaweiMessagingService() =
        HuaweiService(messaging = HmsMessaging.getInstance(application))
}
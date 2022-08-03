package com.gateway.gms.di

import android.app.Application
import com.gateway.gms.data.*
import com.gateway.gms.domain.interfaces.CloudMessaging
import com.gateway.gms.domain.models.Services
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.push.HmsMessaging
import timber.log.Timber

object ServiceLocator {
    @Volatile
    private lateinit var application: Application
    private val googleService: CloudMessaging by lazy { provideGoogleMessagingService() }
    private val huaweiService: CloudMessaging by lazy { provideHuaweiMessagingService() }
    private val googleApiAvailability: GoogleApiAvailability by lazy { provideGoogleApiAvailability() }
    private val huaweiApiAvailability: HuaweiApiAvailability by lazy { provideHuaweiApiAvailability() }
    val cloudMessagingRepository: CloudMessagingRepository by lazy { provideCloudMessagingRepository() }

    /**
     * This function should called in MainActivity

     * to provide an application instance for the service.
     *
     * @author Ahmed Mones
     */
    fun initializeService(application: Application) {
        if (this::application.isInitialized.not()) {
            synchronized(application) { this.application = application }
            prepareAvailability()
        }
    }

    private fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()

    private fun provideHuaweiApiAvailability() = HuaweiApiAvailability.getInstance()

    private fun provideGoogleMessagingService() =
        GoogleService(messaging = FirebaseMessaging.getInstance())

    private fun provideHuaweiMessagingService() =
        HuaweiService(messaging = HmsMessaging.getInstance(application))

    private fun provideCloudMessagingRepository() : CloudMessagingRepository = CloudMessagingRepositoryImpl(
        service = when (ServiceAvailability.serviceProvider) {
            is Services.Google -> googleService
            is Services.Huawei -> huaweiService
            else -> NoneService()
        }
    )

    private fun prepareAvailability() {
        ServiceAvailability.isServicesAvailable = with(ServiceLocator) {
            when (ConnectionResult.SUCCESS) {
                googleApiAvailability.isGooglePlayServicesAvailable(application) -> setServiceProvider(
                    service = Services.Google
                )
                huaweiApiAvailability.isHuaweiMobileServicesAvailable(application) -> setServiceProvider(
                    service = Services.Huawei
                )
                else -> false
            }
        }
    }

    private fun setServiceProvider(service: Services) = true.also {
        Timber.d(service::class.java.name.substringAfter('$'))
        ServiceAvailability.serviceProvider = service
    }
}
package com.gateway.gms.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.gateway.gms.data.*
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import com.gateway.gms.domain.models.Services
import com.gateway.gms.utils.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.push.HmsMessaging
import timber.log.Timber

object GMServiceLocator {
    @Volatile
    private lateinit var application: Application
    val googleService: FirebaseMessaging by lazy { FirebaseMessaging.getInstance() }
    val huaweiService: HmsMessaging by lazy { HmsMessaging.getInstance(application) }
    private val googleApiAvailability: GoogleApiAvailability by lazy { provideGoogleApiAvailability() }
    private val huaweiApiAvailability: HuaweiApiAvailability by lazy { provideHuaweiApiAvailability() }
    val cloudMessagingRepository: CloudMessagingRepository by lazy { provideCloudMessagingRepository() }
    val sharedPref: SharedPreferences by lazy { provideSharedPref() }

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

    private fun provideSharedPref() =
        application.getSharedPreferences(Constants.SharedPref.FILE_KEY, Context.MODE_PRIVATE)

    private fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()

    private fun provideHuaweiApiAvailability() = HuaweiApiAvailability.getInstance()

    private fun provideCloudMessagingRepository(): CloudMessagingRepository =
        CloudMessagingRepositoryImpl(
            service = when (ServiceAvailability.serviceProvider) {
                is Services.Google -> GoogleService()
                is Services.Huawei -> HuaweiService()
                else -> NoneService()
            }
        )

    private fun prepareAvailability() {
        ServiceAvailability.isServicesAvailable = with(GMServiceLocator) {
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
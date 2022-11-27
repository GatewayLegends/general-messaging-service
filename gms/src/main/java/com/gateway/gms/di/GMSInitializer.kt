package com.gateway.gms.di

import android.content.Context
import com.gateway.gms.data.*
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.gateway.gms.domain.models.Services
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.FirebaseApp
import timber.log.Timber

internal class GMSInitializer(private val context: Context, private val listener: CloudMessagingServiceListener? = null) {
    var serviceProvider: Services = Services.None
    var isServicesAvailable: Boolean = false
    private var module: GMSModule
    val repository: CloudMessagingRepository

    init {
        module = GMSModule(context = context)
        prepareAvailability(module = module)
        repository = provideCloudMessagingRepository(module)
    }

    private fun prepareAvailability(module: GMSModule) {
        isServicesAvailable = with(module) {
            when (ConnectionResult.SUCCESS) {
                googleApiAvailability.isGooglePlayServicesAvailable(context) -> {
                    FirebaseApp.initializeApp(context)
                    setServiceProvider(
                        service = Services.Google
                    )
                }
                huaweiApiAvailability.isHuaweiMobileServicesAvailable(context) -> setServiceProvider(
                    service = Services.Huawei
                )
                else -> false
            }
        }
    }

    private fun setServiceProvider(service: Services) = true.also {
        Timber.d(service::class.java.name.substringAfter('$'))
        serviceProvider = service
    }

    private fun provideCloudMessagingRepository(module: GMSModule): CloudMessagingRepository =
        CloudMessagingRepositoryImpl(
            service = when (serviceProvider) {
                is Services.Google -> GoogleService().apply {
                    messaging = module.googleService
                    listener = this@GMSInitializer.listener
                    sharedPreferences = module.sharedPreferences
                }
                is Services.Huawei -> HuaweiService().apply {
                    messaging = module.huaweiService
                    listener = this@GMSInitializer.listener
                    sharedPreferences = module.sharedPreferences
                }
                else -> NoneService()
            }
        )
}

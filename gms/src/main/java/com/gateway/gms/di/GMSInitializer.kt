package com.gateway.gms.di

import android.content.Context
import com.gateway.gms.data.*
import com.gateway.gms.domain.interfaces.CloudMessagingRepository
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.gateway.gms.domain.models.Services
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.FirebaseApp
import timber.log.Timber

internal class GMSInitializer(
    context: Context,
    listener: CloudMessagingServiceListener? = null
) {
    var serviceProvider: Services = Services.None
    var isServicesAvailable: Boolean = false
    private var module: GMSModule
    val repository: CloudMessagingRepository

    init {
        module = GMSModule(context = context, listener = listener)
        prepareAvailability(module = module, context = context)
        repository = provideCloudMessagingRepository(module = module)
    }

    private fun prepareAvailability(module: GMSModule, context: Context) {
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
                    }
                is Services.Huawei -> HuaweiService().apply {
                    messaging = module.huaweiService
                }
                else -> NoneService()
            }
        )
}

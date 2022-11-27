package com.gateway.gms.di

import android.content.Context
import android.content.SharedPreferences
import com.gateway.gms.utils.Constants
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.push.HmsMessaging

internal class GMSModule(private val context: Context) {
    val googleService: FirebaseMessaging by lazy { FirebaseMessaging.getInstance() }
    val huaweiService: HmsMessaging by lazy { HmsMessaging.getInstance(context) }
    val googleApiAvailability: GoogleApiAvailability by lazy { provideGoogleApiAvailability() }
    val huaweiApiAvailability: HuaweiApiAvailability by lazy { provideHuaweiApiAvailability() }
    val sharedPreferences: SharedPreferences by lazy { provideSharedPref() }

    private fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()
    private fun provideHuaweiApiAvailability() = HuaweiApiAvailability.getInstance()

    private fun provideSharedPref() =
        context.getSharedPreferences(Constants.SharedPref.FILE_KEY, Context.MODE_PRIVATE)
}

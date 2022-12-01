package com.gateway.gms.data

import android.content.SharedPreferences
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener

internal object GMSConfigurations {
    var listener: CloudMessagingServiceListener? = null
    var sharedPreferences: SharedPreferences? = null
}

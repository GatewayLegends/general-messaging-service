package com.gateway.gms.data

import com.gateway.gms.domain.models.Services

object ServiceAvailability {
    var serviceProvider: Services = Services.None
    var isServicesAvailable: Boolean = false
}
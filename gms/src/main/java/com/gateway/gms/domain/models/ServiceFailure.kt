package com.gateway.gms.domain.models

sealed interface ServiceFailure {
    data class ServiceNotDetected(
        val message: String? = "No Service Detected",
        val code: Int? = 404
    ) : ServiceFailure

    data class UnknownError(
        val message: String? = "Unknown Error",
        val code: Int? = 520
    ) : ServiceFailure

    data class NoTokenError(
        val message: String? = "No Token Error",
        val code: Int? = 404
    ) : ServiceFailure

    data class FeatureNotProvidedByService(
        val message: String? = "Feature not provided by service!",
        val code: Int? = -1
    ) : ServiceFailure
}

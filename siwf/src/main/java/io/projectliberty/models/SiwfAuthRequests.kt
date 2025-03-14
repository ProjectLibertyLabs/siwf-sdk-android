package io.projectliberty.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an authentication request.
 */
data class GenerateAuthRequest(
    val signedRequest: SignedRequest,
    val additionalCallbackUrlParams: Map<String, String>,
    val options: Options?
)

/**
 * Represents the different types of signed requests.
 */
sealed class SignedRequest {
    @Serializable
    @SerialName("encoded")
    data class SiwfEncodedSignedRequest(val encodedSignedRequest: String) : SignedRequest()

    @Serializable
    @SerialName("signed")
    data class SiwfSignedRequest(
        val requestedSignatures: SiwfRequestedSignature,
        val requestedCredentials: List<SiwfRequestedCredential> = emptyList()
    ) : SignedRequest()
}

/**
 * Options for configuring authentication requests.
 */
data class Options(
    var endpoint: String
)

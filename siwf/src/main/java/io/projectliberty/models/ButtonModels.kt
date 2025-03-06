package io.projectliberty.models

import kotlinx.serialization.SerialName

enum class SiwfButtonMode {
    PRIMARY,
    DARK,
    LIGHT
}

data class GenerateAuthData(
    val signedRequest: SignedRequest,
    val additionalCallbackUrlParams: Map<String, String>,
    val options: SiwfOptions?
)

sealed class SignedRequest {
    @kotlinx.serialization.Serializable
    @SerialName("encoded")
    data class SiwfEncodedSignedRequest(val encodedSignedRequest: String) : SignedRequest()

    @kotlinx.serialization.Serializable
    @SerialName("signed")
    data class SiwfSignedRequest(
        val requestedSignatures: SiwfRequestedSignature,
        val requestedCredentials: List<SiwfCredentialRequest> = emptyList()
    ) : SignedRequest()
}
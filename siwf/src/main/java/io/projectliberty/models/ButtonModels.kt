package io.projectliberty.models

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
    data class SiwfEncodedSignedRequest(val encodedSignedRequest: String) : SignedRequest()
    data class SiwfSignedRequest(
        val signature: SiwfRequestedSignature,
        val credentials: List<SiwfRequestedCredential> = emptyList()
    ) : SignedRequest()
}

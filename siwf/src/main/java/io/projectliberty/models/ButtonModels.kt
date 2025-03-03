package io.projectliberty.models

enum class SiwfButtonMode {
    PRIMARY,
    DARK,
    LIGHT
}

data class GenerateAuthData(
    val signedRequest: SiwfSignedRequest,
    val additionalCallbackUrlParams: Map<String, String>,
    val options: SiwfOptions?
)

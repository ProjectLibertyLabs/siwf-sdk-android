package com.models

enum class LiwlButtonMode {
    PRIMARY,
    DARK,
    LIGHT
}

data class GenerateAuthData(
    val signedRequest: SiwfSignedRequest,
    val additionalCallbackUrlParams: Map<String, String>,
    val options: SiwfOptions?
)

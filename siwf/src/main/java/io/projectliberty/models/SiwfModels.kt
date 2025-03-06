package io.projectliberty.models

import kotlinx.serialization.*

@Serializable
data class SiwfPublicKey(
    val encodedValue: String,
    val encoding: String = "base58",
    val format: String = "ss58",
    val type: String = "Sr25519"
)

@Serializable
data class SiwfSignature(
    val algo: String = "SR25519",
    val encoding: String = "base16",
    val encodedValue: String
)

@Serializable
data class SiwfPayload(
    val callback: String,
    val permissions: List<Int>,
    val userIdentifierAdminUrl: String? = null
)

@Serializable
data class SiwfRequestedSignature(
    val publicKey: SiwfPublicKey,
    val signature: SiwfSignature,
    val payload: SiwfPayload
)

@Serializable
sealed class SiwfCredentialRequest

@Serializable
@SerialName("anyOfRequired")
data class AnyOfRequired(
    val anyOf: List<SiwfCredential>
): SiwfCredentialRequest()

@Serializable
@SerialName("credential")
data class SiwfCredential(
    val type: String,
    val hash: List<String>
): SiwfCredentialRequest()

@Serializable
data class SiwfSignedRequest(
    val requestedSignatures: SiwfRequestedSignature,
    val requestedCredentials: List<SiwfCredentialRequest>? = emptyList()
)

data class SiwfOptions(
    var endpoint: String
)

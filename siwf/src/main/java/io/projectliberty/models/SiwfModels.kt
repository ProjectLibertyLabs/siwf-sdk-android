package io.projectliberty.models

import RequestedCredentialTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Core SIWF data models used for authentication and signing.
 */
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

/**
 * Represents a requested credential in the authentication flow.
 */
@Serializable(with = RequestedCredentialTypeSerializer::class)
sealed class SiwfRequestedCredential

@Serializable
data class SiwfCredential(
    @SerialName("type") val type: String,
    @SerialName("hash") val hash: List<String>
) : SiwfRequestedCredential()

@Serializable
data class AnyOfRequired(
    @SerialName("anyOf") val anyOf: List<SiwfCredential>
) : SiwfRequestedCredential()

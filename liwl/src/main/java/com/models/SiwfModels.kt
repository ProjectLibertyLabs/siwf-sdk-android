package com.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.serializer

@Serializable
data class SiwfPublicKey(
    val encodedValue: String,
    val encoding: String = "base58",
    val format: String = "ss58",
    val type: String = "Sr25519"
)

@Serializable
data class SiwfSignature(
    val encodedValue: String,
    val algo: String = "SR25519",
    val encoding: String = "base16"
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
data class AnyOfRequired(
    val field: String // Example placeholder; update as needed
)

@Serializable
data class SiwfCredential(
    val credentialId: String // Example placeholder; update as needed
)

@Serializable
data class SiwfSignedRequest(
    val requestedSignatures: SiwfRequestedSignature,
    val requestedCredentials: List<SiwfCredential>? = null
)

@Serializable
data class SiwfOptions(
    var endpoint: String,
    var loginMsgUri: String? = null
)

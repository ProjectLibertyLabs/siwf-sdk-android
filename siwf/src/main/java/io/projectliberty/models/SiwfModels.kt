package io.projectliberty.models

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonObject

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

object RequestedCredentialTypeSerializer : KSerializer<SiwfRequestedCredential> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("RequestedCredentialType")

    override fun serialize(encoder: Encoder, value: SiwfRequestedCredential) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("Expected JsonEncoder")

        val jsonElement = when (value) {
            is SiwfCredential -> jsonEncoder.json.encodeToJsonElement(SiwfCredential.serializer(), value)
            is AnyOfRequired -> jsonEncoder.json.encodeToJsonElement(AnyOfRequired.serializer(), value)
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): SiwfRequestedCredential {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return if ("anyOf" in jsonObject) {
            jsonDecoder.json.decodeFromJsonElement(AnyOfRequired.serializer(), jsonObject)
        } else {
            jsonDecoder.json.decodeFromJsonElement(SiwfCredential.serializer(), jsonObject)
        }
    }
}

@Serializable
data class SiwfSignedRequest(
    val requestedSignatures: SiwfRequestedSignature,
    val requestedCredentials: List<SiwfRequestedCredential> = emptyList()
)

data class SiwfOptions(
    var endpoint: String
)

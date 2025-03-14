import io.projectliberty.models.AnyOfRequired
import io.projectliberty.models.SiwfCredential
import io.projectliberty.models.SiwfRequestedCredential
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonObject

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
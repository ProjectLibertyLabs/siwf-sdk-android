package io.projectliberty.helpers

import android.net.Uri
import android.util.Base64
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SignedRequest
import io.projectliberty.models.SiwfSignedRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

enum class EndpointPath(val rawValue: String) {
    START("/start"),
    API_PAYLOAD("/api/payload")
}

private val json = Json { encodeDefaults = true; prettyPrint = false }

fun encodeSignedRequest(request: SiwfSignedRequest): String? {
    return try {
        val jsonString = json.encodeToString(request)
        val data: ByteArray = jsonString.toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(data, Base64.NO_WRAP)
    } catch (e: Exception) {
        println("Error encoding signed request: ${e.message}")
        null
    }
}

fun parseEndpoint(input: String, path: EndpointPath): String {
    return when (input.lowercase()) {
        "mainnet", "production", "prod" ->
            "https://www.frequencyaccess.com/siwa" + path.rawValue
        "testnet", "staging" ->
            "https://testnet.frequencyaccess.com/siwa" + path.rawValue
        else -> input.trim('/') + path.rawValue
    }
}

fun generateAuthenticationUrl(
    authData: GenerateAuthData
): Uri? {
    val encodedSignedRequest = when (authData.signedRequest) {
        is SignedRequest.SiwfEncodedSignedRequest -> authData.signedRequest.encodedSignedRequest
        is SignedRequest.SiwfSignedRequest -> encodeSignedRequest(
            SiwfSignedRequest(
                requestedSignatures = authData.signedRequest.requestedSignatures,
                requestedCredentials = authData.signedRequest.requestedCredentials
            )
        ) ?: return null
    }

    val endpoint = parseEndpoint(authData.options?.endpoint ?: "mainnet", EndpointPath.START)

    val uriBuilder = Uri.parse(endpoint).buildUpon()

    // Filter out reserved query parameters
    val queryItems = authData.additionalCallbackUrlParams.filterKeys { it != "signedRequest" && it != "authorizationCode" }
        .map { Uri.encode(it.key) to Uri.encode(it.value) }

    // Add filtered query parameters
    queryItems.forEach { (key, value) ->
        uriBuilder.appendQueryParameter(key, value)
    }

    // Append signed request last
    uriBuilder.appendQueryParameter("signedRequest", encodedSignedRequest)

    return uriBuilder.build()
}
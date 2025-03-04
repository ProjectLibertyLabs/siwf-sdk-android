package io.projectliberty.helpers

import android.net.Uri
import android.util.Base64
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfSignedRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URL
import java.net.URLEncoder

enum class EndpointPath(val rawValue: String) {
    START("/start"),
    API_PAYLOAD("/api/payload")
}

fun encodeSignedRequest(request: SiwfSignedRequest): String? {
    return try {
        val jsonString = Json.encodeToString(request)
        val data: ByteArray = jsonString.encodeToByteArray()
        return Base64.encodeToString(data, Base64.DEFAULT)
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

fun buildUrlWithQuery(baseUrl: String, queryParams: Map<String, String>): URL? {
    return try {
        val url = URL(baseUrl)
        val separator = if (url.query.isNullOrEmpty()) "?" else "&"
        val query = queryParams.entries.joinToString("&") { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
        }
        URL(baseUrl + separator + query)
    } catch (e: Exception) {
        println("Error building URL: ${e.message}")
        null
    }
}

fun generateAuthenticationUrl(
    authData: GenerateAuthData?,
    authEncodedRequest: String?
): Uri? {
    if (authData?.signedRequest == null && authEncodedRequest == null) {
        println("Error: must pass a signed request or an encoded signed request")
        return null
    }

    val encodedSignedRequest = authEncodedRequest ?: encodeSignedRequest(authData!!.signedRequest)

    val endpoint = parseEndpoint(authData?.options?.endpoint ?: "mainnet", EndpointPath.START)

    val uriBuilder = Uri.parse(endpoint).buildUpon()

    // Filter out reserved query parameters
    val queryItems = authData?.additionalCallbackUrlParams?.filterKeys { it != "signedRequest" && it != "authorizationCode" }
        ?.map { Uri.encode(it.key) to Uri.encode(it.value) }
        ?: emptyList()

    // Add filtered query parameters
    queryItems.forEach { (key, value) ->
        uriBuilder.appendQueryParameter(key, value)
    }

    // Append signed request last
    uriBuilder.appendQueryParameter("signedRequest", encodedSignedRequest)

    return uriBuilder.build()
}
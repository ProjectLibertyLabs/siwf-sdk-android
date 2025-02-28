package com.helpers

import android.util.Base64
import com.models.GenerateAuthData
import com.models.SiwfSignedRequest
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

fun generateAuthenticationUrl(authData: GenerateAuthData): URL? {
    val (signedRequest, additionalCallbackUrlParams, options ) = authData
    val encodedSignedRequest = encodeSignedRequest(signedRequest) ?: return null

    val endpoint = parseEndpoint(options?.endpoint ?: "mainnet", EndpointPath.START)

    // Build query parameters while excluding reserved keywords.
    val queryItems = LinkedHashMap<String, String>()
    additionalCallbackUrlParams.forEach { (key, value) ->
        if (key.lowercase() != "signedrequest" && key.lowercase() != "authorizationcode") {
            queryItems[key] = value
        }
    }
    // Ensure the signedRequest parameter is added last.
    queryItems["signedRequest"] = encodedSignedRequest

    return buildUrlWithQuery(endpoint, queryItems)
}

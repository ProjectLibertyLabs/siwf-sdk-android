package io.projectliberty.helpers

import android.net.Uri
import android.util.Base64
import android.util.Log
import io.projectliberty.models.GenerateAuthRequest
import io.projectliberty.models.SignedRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val TAG = "AuthLoginUtils"

enum class AuthEndpoint(val path: String) {
    START("/start"),
    API_PAYLOAD("/api/payload")
}

private val json = Json { encodeDefaults = true; prettyPrint = false }

/**
 * Encodes a SIWF signed request into a Base64-encoded string.
 *
 * @param request The signed authentication request.
 * @return Base64 encoded string or null if an error occurs.
 */
fun encodeSignedRequest(request: SignedRequest.SiwfSignedRequest): String? {
    return try {
        val jsonString = json.encodeToString(request)
        val data: ByteArray = jsonString.toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(data, Base64.NO_WRAP)
    } catch (e: Exception) {
        Log.e(TAG, "❌ Error encoding signed request: ${e.localizedMessage}", e)
        null
    }
}

/**
 * Determines the correct authentication endpoint based on the input environment.
 *
 * @param environment The environment name (e.g., "mainnet", "testnet").
 * @param endpoint The API path from [AuthEndpoint].
 * @return The full URL to the authentication endpoint.
 */
fun resolveAuthEndpoint(environment: String, endpoint: AuthEndpoint): String {
    return when (environment.lowercase()) {
        "mainnet", "production", "prod" ->
            "https://www.frequencyaccess.com/siwa${endpoint.path}"
        "testnet", "staging" ->
            "https://testnet.frequencyaccess.com/siwa${endpoint.path}"
        else -> {
            val customEndpoint = environment.trimEnd('/')
            Log.w(TAG, "⚠️ Using custom authentication endpoint: $customEndpoint")
            "$customEndpoint${endpoint.path}"
        }
    }
}

/**
 * Generates an authentication URL with query parameters for a sign-in request.
 *
 * @param authRequest The authentication request object containing details.
 * @return A Uri object representing the final authentication URL.
 * @throws Exception If encoding the signed request fails.
 */
fun generateAuthUrl(authRequest: GenerateAuthRequest): Uri {
    val encodedSignedRequest = when (val request = authRequest.signedRequest) {
        is SignedRequest.SiwfEncodedSignedRequest -> request.encodedSignedRequest
        is SignedRequest.SiwfSignedRequest -> {
            encodeSignedRequest(
                SignedRequest.SiwfSignedRequest(
                    requestedSignatures = request.requestedSignatures,
                    requestedCredentials = request.requestedCredentials
                )
            ) ?: throw Exception("❌ Error encoding signed request.")
        }
    }

    val authEndpoint = resolveAuthEndpoint(authRequest.options?.endpoint ?: "mainnet", AuthEndpoint.START)
    val uriBuilder = Uri.parse(authEndpoint).buildUpon()

    // Filter out reserved query parameters to avoid conflicts
    val filteredParams = authRequest.additionalCallbackUrlParams.filterKeys {
        it != "signedRequest" && it != AuthConstants.AUTH_INTENT_KEY
    }

    // Add filtered query parameters to the URL
    filteredParams.forEach { (key, value) ->
        uriBuilder.appendQueryParameter(Uri.encode(key), Uri.encode(value))
    }

    uriBuilder.appendQueryParameter("signedRequest", encodedSignedRequest)

    val finalUri = uriBuilder.build()
    Log.d(TAG, "🔗 Generated authentication URL: $finalUri")

    return finalUri
}

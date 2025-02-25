package com.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class Assets(
    val colors: Colors,
    val content: Content,
    val images: Images
)

@Serializable
data class Colors(
    val primary: String,
    val light: String,
    val dark: String
)

@Serializable
data class Content(
    val title: String
)

@Serializable
data class Images(
    val logoPrimary: String,
    val logoLight: String,
    val logoDark: String
)

// This suspend function fetches the assets from the URL asynchronously.
suspend fun fetchAssets(): Assets? {
    val urlString = "https://projectlibertylabs.github.io/siwf/v2/assets/assets.json"
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                connect()
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonResponse = inputStream.bufferedReader().use { it.readText() }
                    Json { ignoreUnknownKeys = true }.decodeFromString<Assets>(jsonResponse)
                } else {
                    println("Error: HTTP $responseCode")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error fetching data: ${e.message}")
            null
        }
    }
}

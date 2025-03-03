package io.projectliberty.helpers

import android.util.Log
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

suspend fun fetchAssets(): Assets? = withContext(Dispatchers.IO) {
    val urlString = "https://projectlibertylabs.github.io/siwf/v2/assets/assets.json"
    try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val json = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()
            Json { ignoreUnknownKeys = true }.decodeFromString<Assets>(json)
        } else {
            Log.e("fetchAssets", "Error: ${connection.responseCode}")
            connection.disconnect()
            null
        }
    } catch (e: Exception) {
        Log.e("fetchAssets", "Error fetching assets: $e")
        null
    }
}

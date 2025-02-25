package com.liwl

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.models.Assets
import com.models.LiwlButtonMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun LiwlButton(
    mode: LiwlButtonMode = LiwlButtonMode.PRIMARY,
    authUrl: String,
    handleAction: () -> Unit
) {
    // Compose state variables similar to @State properties in SwiftUI
    var title by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.Gray) }
    var textColor by remember { mutableStateOf(Color.White) }
    var borderColor by remember { mutableStateOf(Color.Gray) }
    var logoImage by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var showSafariView by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Fetch assets when the composable appears
    LaunchedEffect(Unit) {
        fetchAssets { assets ->
            title = assets.content.title

            when (mode) {
                LiwlButtonMode.PRIMARY -> {
                    backgroundColor = Color(android.graphics.Color.parseColor(assets.colors.primary))
                    textColor = Color(android.graphics.Color.parseColor(assets.colors.light))
                    borderColor = Color(android.graphics.Color.parseColor(assets.colors.primary))
                    assets.images.logoPrimary?.let { logoBase64 ->
                        val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                        logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            ?.asImageBitmap()
                    }
                }
                LiwlButtonMode.DARK -> {
                    backgroundColor = Color(android.graphics.Color.parseColor(assets.colors.dark))
                    textColor = Color(android.graphics.Color.parseColor(assets.colors.light))
                    borderColor = Color(android.graphics.Color.parseColor(assets.colors.dark))
                    assets.images.logoLight?.let { logoBase64 ->
                        val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                        logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            ?.asImageBitmap()
                    }
                }
                LiwlButtonMode.LIGHT -> {
                    backgroundColor = Color(android.graphics.Color.parseColor(assets.colors.light))
                    textColor = Color(android.graphics.Color.parseColor(assets.colors.dark))
                    borderColor = Color(android.graphics.Color.parseColor(assets.colors.dark))
                    assets.images.logoDark?.let { logoBase64 ->
                        val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                        logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            ?.asImageBitmap()
                    }
                }
            }
        }
    }

    // Button UI
    Button(
        onClick = { openUrl(context, authUrl) },
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (logoImage != null) {
                Image(
                    bitmap = logoImage!!,
                    contentDescription = "Logo",
                    modifier = Modifier.size(33.dp)
                )
            } else {
                Text("🔄", fontSize = 24.sp)
            }
            Text(text = title, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

private fun openUrl(context: Context, url: String) {
    if (url.isNotBlank()) {
        CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url))
    }
}

// A suspend function to fetch assets from the provided URL.
suspend fun fetchAssets(onResult: (Assets) -> Unit) {
    val urlString = "https://projectlibertylabs.github.io/siwf/v2/assets/assets.json"
    try {
        val url = URL(urlString)
        withContext(Dispatchers.IO) {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                val assets = Json { ignoreUnknownKeys = true }.decodeFromString<Assets>(json)
                withContext(Dispatchers.Main) {
                    onResult(assets)
                }
            } else {
                Log.e("fetchAssets", "Error: ${connection.responseCode}")
            }
            connection.disconnect()
        }
    } catch (e: Exception) {
        Log.e("fetchAssets", "Error fetching assets: $e")
    }
}

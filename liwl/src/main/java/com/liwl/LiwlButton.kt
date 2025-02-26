package com.liwl

import android.content.Context
import android.graphics.BitmapFactory
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helpers.fetchAssets
import com.models.LiwlButtonMode
import java.net.URL

@Composable
fun LiwlButton(
    mode: LiwlButtonMode = LiwlButtonMode.PRIMARY,
    authUrl: String,
    handleAction: () -> Unit
) {
    // Compose state variables (similar to SwiftUI @State properties)
    var title by remember { mutableStateOf("Sign In") }
    var backgroundColor by remember { mutableStateOf(Color.Gray) }
    var textColor by remember { mutableStateOf(Color.White) }
    var borderColor by remember { mutableStateOf(Color.Gray) }
    var logoImage by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    val context = LocalContext.current

    // Validate authUrl by attempting to create a URL instance
    val isValidUrl = try {
        Log.e("LiwlButton", authUrl)
        URL(authUrl)
        true
    } catch (e: Exception) {
        false
    }

    // Fetch assets asynchronously when this composable enters composition
    LaunchedEffect(Unit) {
        val assets = fetchAssets()
        if (assets == null) {
            Log.e("LiwlButton", "Failed to fetch assets")
            return@LaunchedEffect
        }

        Log.d("LiwlButton", "Fetched assets: ${assets.images.logoPrimary}")
        Log.d("LiwlButton", "Fetched assets: ${assets.content.title}")
        title = assets.content.title

        when (mode) {
            LiwlButtonMode.PRIMARY -> {
                backgroundColor = Color(android.graphics.Color.parseColor("${assets.colors.primary}"))
                textColor = Color(android.graphics.Color.parseColor("#000000"))
                borderColor = Color(android.graphics.Color.parseColor("${assets.colors.primary}"))
                assets.images.logoPrimary.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            }
            LiwlButtonMode.DARK -> {
                backgroundColor = Color(android.graphics.Color.parseColor("#ffffff"))
                textColor = Color(android.graphics.Color.parseColor("#000000"))
                borderColor = Color(android.graphics.Color.parseColor("#ffffff"))
                assets.images.logoLight.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            }
            LiwlButtonMode.LIGHT -> {
                backgroundColor = Color(android.graphics.Color.parseColor("#000000"))
                textColor = Color(android.graphics.Color.parseColor("#ffffff"))
                borderColor = Color(android.graphics.Color.parseColor("#ffffff"))
                assets.images.logoDark.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    logoImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            }
        }
    }

    // Button UI: the button is disabled if authUrl is not valid.
    Button(
        onClick = {
            handleAction()
            openUrl(context, authUrl)
        },
        shape = RoundedCornerShape(24.dp),
        enabled = isValidUrl
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

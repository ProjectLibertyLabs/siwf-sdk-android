package io.projectliberty.siwf

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.projectliberty.helpers.fetchAssets
import io.projectliberty.helpers.getButtonStyle
import io.projectliberty.helpers.getLocalAssets
import io.projectliberty.models.SiwfButtonMode

@Composable
fun SiwfButton(
    mode: SiwfButtonMode,
    authUrl: Uri,
) {
    val localAssets = getLocalAssets()
    // Set default button styles to locally saved assets
    var buttonStyle by remember { mutableStateOf(getButtonStyle(mode, localAssets)) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val assets = fetchAssets()
        if (assets != null) {
            // If we get remote remote assets, set button styles to latest
            buttonStyle = getButtonStyle(mode, assets)
            return@LaunchedEffect
        }
    }

    // Button UI: the button is disabled if authUrl is not valid.
    Button(
        onClick = { try {
            Log.d("SiwfButton", "Creating new tab.....")
            openUrl(context, authUrl)
            Log.d("SiwfButton", "Created new tab.")
        } catch (e: Exception) {
            Log.e("SiwfButton", "Error creating new tab.")
            val browserIntent = Intent(Intent.ACTION_VIEW, authUrl)
            context.startActivity(browserIntent)
        }
        },
        colors = ButtonDefaults.buttonColors(buttonStyle.backgroundColor),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, buttonStyle.borderColor),
        modifier = Modifier
            .padding(8.dp)
            .height(50.dp),
        enabled = authUrl.toString().isNotBlank()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (buttonStyle.logoImage != null) {
                Image(
                    bitmap = buttonStyle.logoImage!!,
                    contentDescription = "Logo",
                    modifier = Modifier.size(33.dp)
                )
            } else {
                Text("🔄", fontSize = 24.sp)
            }
            Text(text = buttonStyle.title, fontWeight = FontWeight.Bold, color = buttonStyle.textColor)
        }
    }
}

private fun openUrl(context: Context, url: Uri) {
    val ct = CustomTabsIntent.Builder().build()
    ct.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    ct.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ct.launchUrl(context, url)
}

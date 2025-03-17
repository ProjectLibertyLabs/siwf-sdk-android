package io.projectliberty.siwf

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
import io.projectliberty.helpers.fallbackOpenUrl
import io.projectliberty.helpers.getButtonStyle
import io.projectliberty.helpers.getLocalAssets
import io.projectliberty.helpers.getRemoteAssets
import io.projectliberty.helpers.openUrl
import io.projectliberty.models.SiwfButtonMode

private const val TAG = "SIWF.SiwfButton"

/**
 * A customizable Sign-In With Frequency (SIWF) button.
 *
 * @param mode The visual style of the button (Primary, Dark, Light).
 * @param authUrl The authentication URL that the button triggers.
 */
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
        Log.d(TAG, "⏳ Fetching SIWF assets...")
        val remoteAssets = getRemoteAssets()
        if (remoteAssets != null) {
            // If we get remote assets, set button styles to latest
            buttonStyle = getButtonStyle(mode, remoteAssets)
            return@LaunchedEffect
        }
    }

    // Button UI: the button is disabled if authUrl is not valid.
    Button(
        onClick = {
            Log.d(TAG, "🔗 Opening authentication URL: $authUrl")
            try {
                openUrl(context, authUrl)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error opening authentication URL: ${e.localizedMessage}")
                fallbackOpenUrl(context, authUrl)
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

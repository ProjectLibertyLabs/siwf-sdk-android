package io.projectliberty.helpers

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

private const val TAG = "SIWF.SiwfAssetsLoader"

/**
 * Decodes a Base64-encoded image string into an ImageBitmap.
 *
 * @param base64String The Base64-encoded image.
 * @return The decoded ImageBitmap, or null if decoding fails.
 */
fun decodeBase64Image(base64String: String?): ImageBitmap? {
    if (base64String.isNullOrEmpty()) {
        Log.e(TAG, "⚠️ Base64 string is empty or null.")
        return null
    }

    return try {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
    } catch (e: Exception) {
        Log.e(TAG, "❌ Error decoding Base64 image: ${e.localizedMessage}", e)
        null
    }
}

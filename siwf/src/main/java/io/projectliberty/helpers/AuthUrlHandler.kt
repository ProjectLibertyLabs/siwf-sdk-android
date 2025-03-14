package io.projectliberty.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent

private const val TAG = "SIWF.SiwfUrlHandler"

/**
 * Opens a given URL using Chrome Custom Tabs.
 *
 * @param context The application context.
 * @param url The URL to open.
 */
fun openUrl(context: Context, url: Uri) {
    Log.d(TAG, "Attempting to open URL in Custom Tabs: $url")
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    customTabsIntent.launchUrl(context, url)
}

/**
 * Fallback method to open a URL in the default browser if Custom Tabs fail.
 *
 * @param context The application context.
 * @param url The URL to open.
 */
fun fallbackOpenUrl(context: Context, url: Uri) {
    Log.w(TAG, "⚠️ Falling back to opening URL in external browser: $url")
    val browserIntent = Intent(Intent.ACTION_VIEW, url).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(browserIntent)
}

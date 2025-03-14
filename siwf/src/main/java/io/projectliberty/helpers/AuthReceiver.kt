package io.projectliberty.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "SIWF.AuthReceiver"

class AuthReceiver(private val onAuthReceived: (String?) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            Log.w(TAG, "⚠️ Received null intent in broadcast.")
            return
        }

        val authorizationCode = intent.getStringExtra(AuthConstants.AUTH_INTENT_KEY)

        if (authorizationCode.isNullOrEmpty()) {
            Log.w(TAG, "⚠️ No authorization code found in broadcast intent.")
            return
        }

        Log.d(TAG, "✅ Authorization Code Received: $authorizationCode")

        // Trigger the callback with the extracted authorization code
        onAuthReceived(authorizationCode)
    }
}

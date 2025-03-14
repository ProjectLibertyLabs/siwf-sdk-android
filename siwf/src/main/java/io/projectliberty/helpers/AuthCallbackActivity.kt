package io.projectliberty.helpers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

private const val TAG = "AuthCallbackActivity"

class AuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(TAG, "🔄 Received new intent")
        handleDeepLink(intent)
    }

    /**
     * Handles incoming deep link intents and extracts the authorization code.
     * If a valid auth code is found, it is broadcasted to the app.
     *
     * @param intent The incoming intent containing the deep link URI.
     */
    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data == null) {
            Log.w(TAG, "⚠️ No deep link data found in intent.")
            finish()
            return
        }

        Log.d(TAG, "🔗 Deep Link received: $data")

        // Extract authorization code from query parameters
        val authenticationCode = data.getQueryParameter(AuthConstants.AUTH_INTENT_KEY)
        if (authenticationCode.isNullOrEmpty()) {
            Log.w(TAG, "⚠️ No authorization code found in deep link.")
        } else {
            Log.d(TAG, "✅ Authorization Code Extracted: $authenticationCode")

            // Broadcast the auth code to other parts of the app
            sendAuthenticationCodeBroadcast(authenticationCode)
        }

        finish() // Close the activity after handling the deep link
    }

    /**
     * Sends a broadcast with the received authorization code.
     *
     * @param authenticationCode The extracted authorization code.
     */
    private fun sendAuthenticationCodeBroadcast(authenticationCode: String) {
        Log.i(TAG, "📡 Broadcasting auth code to app components.")
        val broadcastIntent = Intent(AuthConstants.AUTH_RESULT_ACTION).apply {
            setPackage(packageName)
            putExtra(AuthConstants.AUTH_INTENT_KEY, authenticationCode)
        }
        sendBroadcast(broadcastIntent)
    }
}

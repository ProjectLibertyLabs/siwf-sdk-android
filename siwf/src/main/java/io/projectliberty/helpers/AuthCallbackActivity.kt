package io.projectliberty.helpers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class AuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null) {
            Log.d("AuthCallback", "Deep Link received: $data")

            val authCode = data.getQueryParameter("authorizationCode")
            if (authCode != null) {
                Log.d("AuthCallback", "Auth Code: $authCode")

                val broadcastIntent = Intent("com.example.siwf.AUTH_RESULT").apply {
                    setPackage(packageName)
                    putExtra("authorizationCode", authCode)
                }
                sendBroadcast(broadcastIntent)
            }
        }
        finish()
    }
}

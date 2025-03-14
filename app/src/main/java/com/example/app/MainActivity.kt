package com.example.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import io.projectliberty.helpers.AuthConstants

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var authenticationCode by remember { mutableStateOf<String?>(null) }

            // Broadcast Receiver to listen for authentication results
            val authReceiver = remember {
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val receivedCode = intent?.getStringExtra(AuthConstants.AUTH_INTENT_KEY)
                        authenticationCode = receivedCode
                        Log.d(TAG, "✅ Authentication code received: $receivedCode")
                    }
                }
            }

            // Intent filter to listen for authentication results
            val authFilter = IntentFilter(AuthConstants.AUTH_RESULT_ACTION)

            // Register the receiver dynamically
            ContextCompat.registerReceiver(
                this,
                authReceiver,
                authFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            // UI Content
            Surface {
                ContentView(
                    authenticationCode = authenticationCode,
                    onDismiss = { authenticationCode = null }
                )
            }

            // Cleanup - Unregister the receiver when the component is disposed
            DisposableEffect(Unit) {
                onDispose {
                    unregisterReceiver(authReceiver)
                    Log.d(TAG, "Unregistered BroadcastReceiver")
                }
            }
        }
    }
}

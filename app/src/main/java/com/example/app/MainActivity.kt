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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var receivedAuthCode by remember { mutableStateOf<String?>(null) }
            val filter = IntentFilter("io.projectliberty.helpers.AUTH_RESULT")
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    receivedAuthCode = intent?.getStringExtra("authorizationCode")
                    Log.d("HostApp", "✅ Received Auth Code: $receivedAuthCode")
                }
            }

            ContextCompat.registerReceiver(
                this,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            Surface {
                ContentView(
                    authCode = receivedAuthCode,
                    onDismiss = { receivedAuthCode = null }
                )
            }

            DisposableEffect(Unit) {
                onDispose { unregisterReceiver(receiver) }
            }
        }
    }
}

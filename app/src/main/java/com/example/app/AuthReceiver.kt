package com.example.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AuthReceiver(private val onAuthReceived: (String?) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val authCode = intent?.getStringExtra("authorizationCode")
        onAuthReceived(authCode)
    }
}

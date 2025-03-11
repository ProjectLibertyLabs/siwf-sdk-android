package com.example.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import io.projectliberty.models.AnyOfRequired
import io.projectliberty.siwf.Siwf
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.models.SiwfPayload
import io.projectliberty.models.SiwfPublicKey
import io.projectliberty.models.SiwfRequestedSignature
import io.projectliberty.models.SiwfSignature
import io.projectliberty.models.SignedRequest
import io.projectliberty.models.SiwfCredential
import io.projectliberty.models.SiwfOptions

@Composable
fun ContentView() {
    val exampleRequest = SignedRequest.SiwfSignedRequest(
        requestedSignatures = SiwfRequestedSignature(
            publicKey = SiwfPublicKey(encodedValue = "f6cn3CiVQjDjPFhSzHxZC94TJg3A5MY6QBNJRezgCmSUSjw7R"),
            signature = SiwfSignature(encodedValue = "0x52eec2145b5e2ec092a7592ba0ac669b9a05bed43c9144cdee5b3a9f727fde343888a6eccac6848362f5bd6ea4792a7bf160d07e31e9dcd9600ab4433a4d788b"),
            payload = SiwfPayload(
                callback = "http://localhost:3000/login/callback",
                permissions = listOf(7, 8, 9, 10)
            )
        ),
        requestedCredentials = listOf(
            SiwfCredential(
                type = "VerifiedGraphKeyCredential",
                hash = listOf("bciqmdvmxd54zve5kifycgsdtoahs5ecf4hal2ts3eexkgocyc5oca2y")
            ),
            AnyOfRequired(
                anyOf = listOf(
                    SiwfCredential(
                        type = "VerifiedEmailAddressCredential",
                        hash = listOf("bciqe4qoczhftici4dzfvfbel7fo4h4sr5grco3oovwyk6y4ynf44tsi")
                    ),
                    SiwfCredential(
                        type = "VerifiedPhoneNumberCredential",
                        hash = listOf("bciqjspnbwpc3wjx4fewcek5daysdjpbf5xjimz5wnu5uj7e3vu2uwnq")
                    )
                )
            )
        )
    )

    val encodedSignedRequest = SignedRequest.SiwfEncodedSignedRequest(
        encodedSignedRequest = "eyJyZXF1ZXN0ZWRTaWduYXR1cmVzIjp7InB1YmxpY0tleSI6eyJlbmNvZGVkVmFsdWUiOiJmNmNuM0NpVlFqRGpQRmhTekh4WkM5NFRKZzNBNU1ZNlFCTkpSZXpnQ21TVVNqdzdSIiwiZW5jb2RpbmciOiJiYXNlNTgiLCJmb3JtYXQiOiJzczU4IiwidHlwZSI6IlNyMjU1MTkifSwic2lnbmF0dXJlIjp7ImFsZ28iOiJTUjI1NTE5IiwiZW5jb2RpbmciOiJiYXNlMTYiLCJlbmNvZGVkVmFsdWUiOiIweGNlYjA5ZDljNTBiNjZlZGZkNGZlYTBlYzI1MTU3NTQ4NTdiNTAwYmExYWY0NzI1MzE4Y2MxYTdmYzE1YWVmMDhlOGRmMmM4YTE1NjA2ODNmM2JjZTA2MzBhYWVlMTU5NjQ4YTMwNWY0NTJkZTc1MTk3OGE0N2RhNTY4MjM1ZTgzIn0sInBheWxvYWQiOnsiY2FsbGJhY2siOiJzaXdmZGVtb2FwcDovL2xvZ2luIiwicGVybWlzc2lvbnMiOls3LDgsOSwxMF19fSwicmVxdWVzdGVkQ3JlZGVudGlhbHMiOlt7InR5cGUiOiJWZXJpZmllZEdyYXBoS2V5Q3JlZGVudGlhbCIsImhhc2giOlsiYmNpcW1kdm14ZDU0enZlNWtpZnljZ3NkdG9haHM1ZWNmNGhhbDJ0czNlZXhrZ29jeWM1b2NhMnkiXX0seyJhbnlPZiI6W3sidHlwZSI6IlZlcmlmaWVkRW1haWxBZGRyZXNzQ3JlZGVudGlhbCIsImhhc2giOlsiYmNpcWU0cW9jemhmdGljaTRkemZ2ZmJlbDdmbzRoNHNyNWdyY28zb292d3lrNnk0eW5mNDR0c2kiXX0seyJ0eXBlIjoiVmVyaWZpZWRQaG9uZU51bWJlckNyZWRlbnRpYWwiLCJoYXNoIjpbImJjaXFqc3BuYndwYzN3ang0ZmV3Y2VrNWRheXNkanBiZjV4amltejV3bnU1dWo3ZTN2dTJ1d25xIl19XX1dfQ")

    val authData = GenerateAuthData(
            signedRequest = exampleRequest,
            additionalCallbackUrlParams = emptyMap(),
            options = SiwfOptions(
                endpoint = "testnet"
            )
        )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // Primary Button
        Siwf.CreateSignInButton(authData = authData)
        // Dark Button
        Siwf.CreateSignInButton(mode = SiwfButtonMode.DARK, authData = authData)
        // Light Button
        Siwf.CreateSignInButton(mode = SiwfButtonMode.LIGHT, authData = authData)
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register BroadcastReceiver in the Activity (not in Composable)
        val filter = IntentFilter("com.example.siwf.AUTH_RESULT")
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val authCode = intent?.getStringExtra("authorizationCode")
                Log.d("HostApp", "✅ Received Auth Code: $authCode")
            }
        }
        ContextCompat.registerReceiver(
            this, // Use activity context
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED // Required flag for Android 14+
        )

        setContent {
            ContentView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver) // Unregister to prevent leaks
    }
}
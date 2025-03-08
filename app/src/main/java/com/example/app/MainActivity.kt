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
        encodedSignedRequest = "eyJyZXF1ZXN0ZWRTaWduYXR1cmVzIjp7InB1YmxpY0tleSI6eyJlbmNvZGVkVmFsdWUiOiJmNmNuM0NpVlFqRGpQRmhTekh4WkM5NFRKZzNBNU1ZNlFCTkpSZXpnQ21TVVNqdzdSIiwiZW5jb2RpbmciOiJiYXNlNTgiLCJmb3JtYXQiOiJzczU4IiwidHlwZSI6IlNyMjU1MTkifSwic2lnbmF0dXJlIjp7ImFsZ28iOiJTUjI1NTE5IiwiZW5jb2RpbmciOiJiYXNlMTYiLCJlbmNvZGVkVmFsdWUiOiIweDUyZWVjMjE0NWI1ZTJlYzA5MmE3NTkyYmEwYWM2NjliOWEwNWJlZDQzYzkxNDRjZGVlNWIzYTlmNzI3ZmRlMzQzODg4YTZlY2NhYzY4NDgzNjJmNWJkNmVhNDc5MmE3YmYxNjBkMDdlMzFlOWRjZDk2MDBhYjQ0MzNhNGQ3ODhiIn0sInBheWxvYWQiOnsiY2FsbGJhY2siOiJodHRwOi8vbG9jYWxob3N0OjMwMDAvbG9naW4vY2FsbGJhY2siLCJwZXJtaXNzaW9ucyI6WzcsOCw5LDEwXX19LCJyZXF1ZXN0ZWRDcmVkZW50aWFscyI6W3sidHlwZSI6IlZlcmlmaWVkR3JhcGhLZXlDcmVkZW50aWFsIiwiaGFzaCI6WyJiY2lxbWR2bXhkNTR6dmU1a2lmeWNnc2R0b2FoczVlY2Y0aGFsMnRzM2VleGtnb2N5YzVvY2EyeSJdfSx7ImFueU9mIjpbeyJ0eXBlIjoiVmVyaWZpZWRFbWFpbEFkZHJlc3NDcmVkZW50aWFsIiwiaGFzaCI6WyJiY2lxZTRxb2N6aGZ0aWNpNGR6ZnZmYmVsN2ZvNGg0c3I1Z3JjbzNvb3Z3eWs2eTR5bmY0NHRzaSJdfSx7InR5cGUiOiJWZXJpZmllZFBob25lTnVtYmVyQ3JlZGVudGlhbCIsImhhc2giOlsiYmNpcWpzcG5id3BjM3dqeDRmZXdjZWs1ZGF5c2RqcGJmNXhqaW16NXdudTV1ajdlM3Z1MnV3bnEiXX1dfV19")

    val authData = GenerateAuthData(
            signedRequest = encodedSignedRequest,
            additionalCallbackUrlParams = emptyMap(),
            options = SiwfOptions(
                endpoint = "testnet"
            )
        )

//    Receiver for callback
    val context = LocalContext.current
    val filter = IntentFilter("com.example.siwf.AUTH_RESULT")
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val authCode = intent?.getStringExtra("authorizationCode")
            Log.d("HostApp", "Received Auth Code: $authCode")
        }
    }
    ContextCompat.registerReceiver(
        context,
        receiver,
        filter,
        ContextCompat.RECEIVER_NOT_EXPORTED
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView()
        }
    }
}
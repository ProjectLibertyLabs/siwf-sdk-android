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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun ContentView(authCode: String?, onDismiss: () -> Unit) {
    var showDialog by remember(authCode) { mutableStateOf(authCode != null) }

    if (showDialog && authCode != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false; onDismiss() },
            title = { Text("Authorization Code Received") },
            text = { Text("Code: $authCode") },
            confirmButton = {
                Button(onClick = { showDialog = false; onDismiss() }) {
                    Text("OK")
                }
            }
        )
    }

    val exampleRequest = SignedRequest.SiwfSignedRequest(
        requestedSignatures = SiwfRequestedSignature(
            publicKey = SiwfPublicKey(encodedValue = "f6cn3CiVQjDjPFhSzHxZC94TJg3A5MY6QBNJRezgCmSUSjw7R"),
            signature = SiwfSignature(encodedValue = "0xceb09d9c50b66edfd4fea0ec2515754857b500ba1af4725318cc1a7fc15aef08e8df2c8a1560683f3bce0630aaee159648a305f452de751978a47da568235e83"),
            payload = SiwfPayload(
                callback = "siwfdemoapp://login",
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

    val authDataEncoded = GenerateAuthData(
        encodedSignedRequest,
        additionalCallbackUrlParams = emptyMap(),
        options = SiwfOptions(
            endpoint = "testnet"
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // Primary Button with encoded data
        Siwf.CreateSignInButton(authData = authDataEncoded)
        // Dark Button with encoded data
        Siwf.CreateSignInButton(mode = SiwfButtonMode.DARK, authData = authDataEncoded)
        // Light Button with non-encoded data
        Siwf.CreateSignInButton(mode = SiwfButtonMode.LIGHT, authData = authData)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var receivedAuthCode by remember { mutableStateOf<String?>(null) }
            val filter = IntentFilter("com.example.siwf.AUTH_RESULT")
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

package io.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.projectliberty.siwf.Siwf
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.models.SiwfPayload
import io.projectliberty.models.SiwfPublicKey
import io.projectliberty.models.SiwfRequestedSignature
import io.projectliberty.models.SiwfSignature
import io.projectliberty.models.SiwfSignedRequest

@Composable
fun ContentView() {
    val exampleRequest = SiwfSignedRequest(
        requestedSignatures = SiwfRequestedSignature(
            publicKey = SiwfPublicKey(encodedValue = "examplePublicKey"),
            signature = SiwfSignature(encodedValue = "exampleEncodedSignature"),
            payload = SiwfPayload(
                callback = "https://www.google.com",
                permissions = listOf(1, 2, 3)
            )
        )
    )

    val authData = GenerateAuthData(
        signedRequest = exampleRequest,
        additionalCallbackUrlParams = emptyMap(),
        options = null
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // Primary Button
        Siwf.CreateSignInButton(mode = SiwfButtonMode.PRIMARY, authData = authData)
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
package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.liwl.Liwl
import com.models.SiwfPayload
import com.models.SiwfPublicKey
import com.models.SiwfSignature
import com.models.SiwfSignedRequest
import com.models.SiwfRequestedSignature
import com.models.GenerateAuthData
import com.models.LiwlButtonMode
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
        Liwl.CreateSignInButton(mode = LiwlButtonMode.PRIMARY, authData = authData)
        // Dark Button
        Liwl.CreateSignInButton(mode = LiwlButtonMode.DARK, authData = authData)
        // Light Button
        Liwl.CreateSignInButton(mode = LiwlButtonMode.LIGHT, authData = authData)
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
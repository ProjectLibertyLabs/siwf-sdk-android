package com.example.app

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

    val authEncodedRequest = "eyJyZXF1ZXN0ZWRTaWduYXR1cmVzIjp7InB1YmxpY0tleSI6eyJlbmNvZGVkVmFsdWUiOiJmNmJNRVh2ZDZkaHNuM0R1c2V3OGhmUm5Dc0ZlaEpMQzhkTkZpQWpHNEhoeTNFZVd6IiwiZW5jb2RpbmciOiJiYXNlNTgiLCJmb3JtYXQiOiJzczU4IiwidHlwZSI6IlNyMjU1MTkifSwic2lnbmF0dXJlIjp7ImFsZ28iOiJTUjI1NTE5IiwiZW5jb2RpbmciOiJiYXNlMTYiLCJlbmNvZGVkVmFsdWUiOiIweDNjZGJkNGZhY2ZmMmQ3MDIzNzBjNmI5OWMwOGU2MDhhM2YwNGU4MzI4MzdhMmE1NjA3YjRkOTc4ZjJjZjZiNGRmNDJjY2E0MmJjMTc0NzViNTJkY2JkZDViMGIyOGIxOTE4OGNhMzViMzQ0N2U1MmViNjg5MTcyNDY4MzEwZDg2In0sInBheWxvYWQiOnsiY2FsbGJhY2siOiJodHRwczovL2V4YW1wbGUuY29tIiwicGVybWlzc2lvbnMiOls3LDgsOSwxMF19fSwicmVxdWVzdGVkQ3JlZGVudGlhbHMiOlt7ImFueU9mIjpbeyJ0eXBlIjoiVmVyaWZpZWRFbWFpbEFkZHJlc3NDcmVkZW50aWFsIiwiaGFzaCI6WyJiY2lxZTRxb2N6aGZ0aWNpNGR6ZnZmYmVsN2ZvNGg0c3I1Z3JjbzNvb3Z3eWs2eTR5bmY0NHRzaSJdfSx7InR5cGUiOiJWZXJpZmllZFBob25lTnVtYmVyQ3JlZGVudGlhbCIsImhhc2giOlsiYmNpcWpzcG5id3BjM3dqeDRmZXdjZWs1ZGF5c2RqcGJmNXhqaW16NXdudTV1ajdlM3Z1MnV3bnEiXX1dfSx7InR5cGUiOiJWZXJpZmllZEdyYXBoS2V5Q3JlZGVudGlhbCIsImhhc2giOlsiYmNpcW1kdm14ZDU0enZlNWtpZnljZ3NkdG9haHM1ZWNmNGhhbDJ0czNlZXhrZ29jeWM1b2NhMnkiXX1dLCJhcHBsaWNhdGlvbkNvbnRleHQiOnsidXJsIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9jb250ZXh0Lmpzb24ifX0"

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // Primary Button
        Siwf.CreateSignInButton(authEncodedRequest = authEncodedRequest)
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
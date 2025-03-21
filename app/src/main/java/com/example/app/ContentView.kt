package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.projectliberty.models.*
import io.projectliberty.siwf.Siwf

/**
 * AuthScreen - Displays sign-in buttons and handles authorization dialog.
 *
 * @param authorizationCode The authorization code received from authentication flow (nullable).
 * @param onDismiss Callback to handle dialog dismissal.
 */
@Composable
fun ContentView(authorizationCode: String?, authorizationUri: String?, onDismiss: () -> Unit) {
    // Show the dialog only if an authorization code is received
    var showDialog by remember(authorizationCode) { mutableStateOf(authorizationCode != null) }

    if (showDialog && authorizationCode != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            title = { Text("Authorization Code Received") },
            text = { Text("Code: $authorizationCode") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }

    val authRequest = createAuthRequest()

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "SIWF Demo App",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth()
        )

        // Primary Sign-In Button (Default)
        Siwf.CreateSignInButton(authRequest = authRequest)

        // Dark-themed Sign-In Button
        Siwf.CreateSignInButton(mode = SiwfButtonMode.DARK, authRequest = authRequest)

        // Light-themed Sign-In Button (Uses non-encoded request)
        Siwf.CreateSignInButton(mode = SiwfButtonMode.LIGHT, authRequest = authRequest)
    }
}

/**
 * Creates an authentication request object with signed request data.
 *
 * @return GenerateAuthRequest containing signed request and authentication options.
 */
fun createAuthRequest(): GenerateAuthRequest {
    // Example signed request with public key, signature, and callback URL
    val signedRequest = SignedRequest.SiwfSignedRequest(
        requestedSignatures = SiwfRequestedSignature(
            publicKey = SiwfPublicKey(encodedValue = "f6cn3CiVQjDjPFhSzHxZC94TJg3A5MY6QBNJRezgCmSUSjw7R"),
            signature = SiwfSignature(encodedValue = "0xceb09d9c50b66edfd4fea0ec2515754857b500ba1af4725318cc1a7fc15aef08e8df2c8a1560683f3bce0630aaee159648a305f452de751978a47da568235e83"),
            payload = SiwfPayload(
                callback = "siwfdemoapp://login",  // Deep link callback for authentication
                permissions = listOf(7, 8, 9, 10) // Example permission list
            )
        ),
        requestedCredentials = listOf(
            // Graph Key Credential (Required)
            SiwfCredential(
                type = "VerifiedGraphKeyCredential",
                hash = listOf("bciqmdvmxd54zve5kifycgsdtoahs5ecf4hal2ts3eexkgocyc5oca2y")
            ),
            // AnyOfRequired - The user must provide at least one of these credentials
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
//        To help users understand which application is asking them to sign in, you can provide
//        an applicationContext object that contains the URL of an application context credential.

//        applicationContext = ApplicationContextUrl(
//            url = "https://example.org/myapp/siwf-manifest.json"
//        )
    )

    // Alternative: Encoded signed request version
    val encodedSignedRequest = SignedRequest.SiwfEncodedSignedRequest(
        encodedSignedRequest = "eyJyZXF1ZXN0ZWRTaWduYXR1cmVzIjp7InB1YmxpY0tleSI6eyJlbmNvZGVkVmFsdWUiOiJmNmNuM0NpVlFqRGpQRmhTekh4WkM5NFRKZzNBNU1ZNlFCTkpSZXpnQ21TVVNqdzdSIiwiZW5jb2RpbmciOiJiYXNlNTgiLCJmb3JtYXQiOiJzczU4IiwidHlwZSI6IlNyMjU1MTkifSwic2lnbmF0dXJlIjp7ImFsZ28iOiJTUjI1NTE5IiwiZW5jb2RpbmciOiJiYXNlMTYiLCJlbmNvZGVkVmFsdWUiOiIweGNlYjA5ZDljNTBiNjZlZGZkNGZlYTBlYzI1MTU3NTQ4NTdiNTAwYmExYWY0NzI1MzE4Y2MxYTdmYzE1YWVmMDhlOGRmMmM4YTE1NjA2ODNmM2JjZTA2MzBhYWVlMTU5NjQ4YTMwNWY0NTJkZTc1MTk3OGE0N2RhNTY4MjM1ZTgzIn0sInBheWxvYWQiOnsiY2FsbGJhY2siOiJzaXdmZGVtb2FwcDovL2xvZ2luIiwicGVybWlzc2lvbnMiOls3LDgsOSwxMF19fSwicmVxdWVzdGVkQ3JlZGVudGlhbHMiOlt7InR5cGUiOiJWZXJpZmllZEdyYXBoS2V5Q3JlZGVudGlhbCIsImhhc2giOlsiYmNpcW1kdm14ZDU0enZlNWtpZnljZ3NkdG9haHM1ZWNmNGhhbDJ0czNlZXhrZ29jeWM1b2NhMnkiXX0seyJhbnlPZiI6W3sidHlwZSI6IlZlcmlmaWVkRW1haWxBZGRyZXNzQ3JlZGVudGlhbCIsImhhc2giOlsiYmNpcWU0cW9jemhmdGljaTRkemZ2ZmJlbDdmbzRoNHNyNWdyY28zb292d3lrNnk0eW5mNDR0c2kiXX0seyJ0eXBlIjoiVmVyaWZpZWRQaG9uZU51bWJlckNyZWRlbnRpYWwiLCJoYXNoIjpbImJjaXFqc3BuYndwYzN3ang0ZmV3Y2VrNWRheXNkanBiZjV4amltejV3bnU1dWo3ZTN2dTJ1d25xIl19XX1dfQ"
    )

    return GenerateAuthRequest(
        signedRequest = signedRequest, // Switch to encodedSignedRequest if needed
//        additionalCallbackUrlParams = emptyMap(),
        additionalCallbackUrlParams = mapOf(
            "abc" to "123"
        ),
        options = Options(endpoint = "testnet") // Switch to "mainnet" if needed
    )
}

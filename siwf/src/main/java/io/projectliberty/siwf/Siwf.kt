package io.projectliberty.siwf

import android.util.Log
import androidx.compose.runtime.Composable
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.helpers.generateAuthenticationUrl

object Siwf {
    @Composable
    fun CreateSignInButton(
        mode: SiwfButtonMode = SiwfButtonMode.PRIMARY,
        authData: GenerateAuthData,
    ) {
        Log.e("DEBUGGGGG*** in Siwf", authData.toString())

        val authUrl = generateAuthenticationUrl(authData)

        Log.e("DEBUGGGGG*** in Siwf", authUrl.toString())

        SiwfButton(
            mode = mode,
            authUrl = authUrl,
        )
    }
}

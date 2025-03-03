package io.projectliberty.siwf

import androidx.compose.runtime.Composable
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.helpers.generateAuthenticationUrl

object Siwf {
    @Composable
    fun CreateSignInButton(
        mode: SiwfButtonMode,
        authData: GenerateAuthData
    ) {
        val authUrl = generateAuthenticationUrl(authData)

        SiwfButton(
            mode = mode,
            authUrl = authUrl.toString(),
        )
    }
}

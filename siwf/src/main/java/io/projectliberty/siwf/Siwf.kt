package io.projectliberty.siwf

import androidx.compose.runtime.Composable
import io.projectliberty.models.GenerateAuthData
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.helpers.generateAuthenticationUrl

object Siwf {
    @Composable
    fun CreateSignInButton(
        mode: SiwfButtonMode = SiwfButtonMode.PRIMARY,
        authData: GenerateAuthData? = null,
        authEncodedRequest: String? = null
    ) {
        val authUrl = generateAuthenticationUrl(authData, authEncodedRequest)

        SiwfButton(
            mode = mode,
            authUrl = authUrl,
        )
    }
}

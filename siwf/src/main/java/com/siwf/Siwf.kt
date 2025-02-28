package com.siwf

import androidx.compose.runtime.Composable
import com.models.GenerateAuthData
import com.models.SiwfButtonMode
import com.helpers.generateAuthenticationUrl

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

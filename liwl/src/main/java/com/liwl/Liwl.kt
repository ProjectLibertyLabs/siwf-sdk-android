package com.liwl

import androidx.compose.runtime.Composable
import com.models.GenerateAuthData
import com.models.LiwlButtonMode
import com.helpers.generateAuthenticationUrl

object Liwl {
    @Composable
    fun CreateSignInButton(
        mode: LiwlButtonMode,
        authData: GenerateAuthData
    ) {
        val authUrl = generateAuthenticationUrl(authData)

        LiwlButton(
            mode = mode,
            authUrl = authUrl.toString(),
        )
    }
}

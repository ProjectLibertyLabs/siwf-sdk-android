package com.liwl

import androidx.compose.runtime.Composable
import com.models.GenerateAuthData
import com.models.LiwlButtonMode
import android.util.Log
import com.helpers.generateAuthenticationUrl


object Liwl {
    @Composable
    fun CreateSignInButton(
        handleAction: () -> Unit,
        mode: LiwlButtonMode,
        authData: GenerateAuthData
    ) {
        Log.d("authData*****", authData.toString())
        val authUrl = generateAuthenticationUrl(authData)
        Log.d("authUrl*****", authUrl.toString())

        LiwlButton(
            mode = mode,
            authUrl = authUrl.toString(),
            handleAction = handleAction
        )
    }
}

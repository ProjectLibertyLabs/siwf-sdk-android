package io.projectliberty.siwf

import android.util.Log
import androidx.compose.runtime.Composable
import io.projectliberty.models.GenerateAuthRequest
import io.projectliberty.models.SiwfButtonMode
import io.projectliberty.helpers.generateAuthUrl

/**
 * Object containing helper functions for Sign-In With Frequency (SIWF) authentication.
 */
object Siwf {

    /**
     * Creates a sign-in button that initiates the authentication process.
     *
     * @param mode The visual style of the button (Primary, Dark, Light).
     * @param authRequest The authentication request containing signed request data and options.
     */
    @Composable
    fun CreateSignInButton(
        mode: SiwfButtonMode = SiwfButtonMode.PRIMARY,
        authRequest: GenerateAuthRequest,
    ) {
        val authUrl = generateAuthUrl(authRequest)

        SiwfButton(
            mode = mode,
            authUrl = authUrl,
        )
    }
}

package io.projectliberty.helpers

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import io.projectliberty.models.SiwfButtonMode

data class ButtonStyles(
    var title: String,
    var backgroundColor: Color,
    var textColor: Color,
    var borderColor: Color,
    var logoImage: androidx.compose.ui.graphics.ImageBitmap?
)


fun getButtonStyle(mode: SiwfButtonMode, assets: Assets): ButtonStyles {
    val title = assets.content.title
    val primaryColor = assets.colors.primary
    val darkColor = assets.colors.dark
    val lightColor = assets.colors.light

    when (mode) {
        SiwfButtonMode.PRIMARY -> {
            return ButtonStyles(
                title,
                backgroundColor = Color(android.graphics.Color.parseColor(primaryColor)),
                textColor = Color(android.graphics.Color.parseColor(lightColor)),
                borderColor = Color(android.graphics.Color.parseColor(primaryColor)),
                logoImage = assets.images.logoPrimary.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            )
        }

        SiwfButtonMode.DARK -> {
            return ButtonStyles(
                title,
                backgroundColor = Color(android.graphics.Color.parseColor(darkColor)),
                textColor = Color(android.graphics.Color.parseColor(lightColor)),
                borderColor = Color(android.graphics.Color.parseColor(darkColor)),
                logoImage = assets.images.logoLight.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            )
        }

        SiwfButtonMode.LIGHT -> {
            return ButtonStyles(
                title,
                backgroundColor = Color(android.graphics.Color.parseColor(lightColor)),
                textColor = Color(android.graphics.Color.parseColor(darkColor)),
                borderColor = Color(android.graphics.Color.parseColor(darkColor)),
                logoImage = assets.images.logoDark.let { logoBase64 ->
                    val imageBytes = Base64.decode(logoBase64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ?.asImageBitmap()
                }
            )
        }
    }
}
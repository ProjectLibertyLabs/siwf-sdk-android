package com.example.liwl

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button

data class LiwlButtonStyleConfig(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color,
    val logo: Int
)

@Composable
fun LiwlButton(
    style: LiwlButtonStyle = LiwlButtonStyle.NORMAL,
    redirectUrl: String = "",
    title: String = "Login with Liberty"
) {
    val context = LocalContext.current

    val styleConfig = when (style) {
        LiwlButtonStyle.NORMAL -> LiwlButtonStyleConfig(
            backgroundColor = Color(0xFF00BFA6),
            textColor = Color.Black,
            borderColor = Color(0xFF00BFA6),
            logo = R.drawable.frequency_logo
        )
        LiwlButtonStyle.DARK -> LiwlButtonStyleConfig(
            backgroundColor = Color.Black,
            textColor = Color.White,
            borderColor = Color.Black,
            logo = R.drawable.frequency_logo_light
        )
        LiwlButtonStyle.LIGHT -> LiwlButtonStyleConfig(
            backgroundColor = Color.White,
            textColor = Color.Black,
            borderColor = Color.Black,
            logo = R.drawable.frequency_logo_dark
        )
    }

    Button(
        onClick = { openUrl(context, redirectUrl) },
        colors = ButtonDefaults.buttonColors(styleConfig.backgroundColor),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, styleConfig.borderColor),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = styleConfig.logo),
                contentDescription = null,
                modifier = Modifier.size(33.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title, color = styleConfig.textColor, fontSize = 16.sp)
        }
    }
}

private fun openUrl(context: Context, url: String) {
    if (url.isNotBlank()) {
        CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url))
    }
}
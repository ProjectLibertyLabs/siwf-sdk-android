package com.helpers

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(hex: String): Color {
    var hexSanitized = hex.trim()
    if (hexSanitized.startsWith("#")) {
        hexSanitized = hexSanitized.substring(1)
    }
    // Handle 3-character hex codes like "000" or "fff"
    if (hexSanitized.length == 3) {
        hexSanitized = hexSanitized.map { "$it$it" }.joinToString("")
    }
    if (hexSanitized.length != 6) {
        return Color.Transparent
    }
    return try {
        val rgbValue = hexSanitized.toLong(16)
        val red = ((rgbValue shr 16) and 0xFF).toInt()
        val green = ((rgbValue shr 8) and 0xFF).toInt()
        val blue = (rgbValue and 0xFF).toInt()
        Color(red / 255f, green / 255f, blue / 255f)
    } catch (e: Exception) {
        Color.Transparent
    }
}

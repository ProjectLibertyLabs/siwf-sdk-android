package com.helpers

// The Base64-URL alphabet as a list of strings.
val TO_BASE64URL: List<String> =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".map { it.toString() }

// A lookup table for decoding Base64-URL characters. Unset entries are -1.
val FROM_BASE64URL: IntArray = run {
    val lookup = IntArray(128) { -1 }
    val base64URLChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    base64URLChars.forEachIndexed { i, c ->
        lookup[c.code] = i
    }
    lookup
}

/**
 * Converts the given string into a Base64-URL encoded string.
 */
fun stringToBase64URL(str: String): String {
    val base64 = mutableListOf<String>()
    var queue = 0
    var queuedBits = 0

    // Emitter function that appends Base64 characters to our result.
    fun emitter(byte: Byte) {
        // Treat the byte as unsigned.
        queue = (queue shl 8) or (byte.toInt() and 0xff)
        queuedBits += 8
        while (queuedBits >= 6) {
            val pos = (queue shr (queuedBits - 6)) and 63
            base64.add(TO_BASE64URL[pos])
            queuedBits -= 6
        }
    }

    // Convert string to UTF-8 bytes and process each byte.
    stringToUTF8(str, ::emitter)

    if (queuedBits > 0) {
        queue = queue shl (6 - queuedBits)
        queuedBits = 6
        while (queuedBits >= 6) {
            val pos = (queue shr (queuedBits - 6)) and 63
            base64.add(TO_BASE64URL[pos])
            queuedBits -= 6
        }
    }

    return base64.joinToString("")
}

/**
 * Decodes a Base64-URL encoded string back into a regular string.
 * Throws an Exception if an invalid character is encountered.
 */
@Throws(Exception::class)
fun stringFromBase64URL(str: String): String {
    val conv = mutableListOf<Char>()

    fun emit(codepoint: Int) {
        // For simplicity, we assume codepoint is within the BMP.
        conv.add(codepoint.toChar())
    }

    val state = UTF8State()
    var queue = 0
    var queuedBits = 0

    for ((i, char) in str.withIndex()) {
        val ascii = char.code
        if (ascii >= 128) {
            throw Exception("Invalid Base64-URL character: $char at position $i")
        }
        val bits = FROM_BASE64URL[ascii]
        if (bits > -1) {
            // Valid Base64-URL character.
            queue = (queue shl 6) or bits
            queuedBits += 6
            while (queuedBits >= 8) {
                val byteVal = (queue shr (queuedBits - 8)) and 0xff
                stringFromUTF8(byteVal, state) { codepoint ->
                    emit(codepoint)
                }
                queuedBits -= 8
            }
        } else if (bits == -2) {
            // (If defined, ignore spaces, tabs, newlines, or '=' characters)
            continue
        } else {
            throw Exception("Invalid Base64-URL character $char at position $i")
        }
    }

    return conv.joinToString("")
}

/**
 * Helper data class to track UTF-8 decoding state.
 */
data class UTF8State(var utf8seq: Int = 0, var codepoint: Int = 0)

/**
 * Decodes a single UTF-8 byte using the current state.
 * - If the byte is ASCII, it is emitted directly.
 * - If it’s a continuation byte, it updates the codepoint.
 * - If it’s the start of a multi-byte sequence, it initializes the state.
 */
fun stringFromUTF8(byte: Int, state: UTF8State, emit: (Int) -> Unit) {
    if (byte and 0x80 == 0) { // 1-byte character (ASCII)
        emit(byte)
    } else if (byte and 0xC0 == 0x80) { // Continuation byte
        state.codepoint = (state.codepoint shl 6) or (byte and 0x3F)
        state.utf8seq -= 1
        if (state.utf8seq == 0) {
            emit(state.codepoint)
        }
    } else { // Start of multi-byte sequence
        val mask = listOf(0x00, 0x7F, 0x1F, 0x0F, 0x07)
        val length = listOf(0, 1, 2, 3, 4)
        val leadingOnes = countLeadingOnes(byte)
        state.utf8seq = if (leadingOnes in length.indices) length[leadingOnes] else 0
        state.codepoint = byte and if (leadingOnes in mask.indices) mask[leadingOnes] else 0
    }
}

/**
 * Converts a string to UTF-8 bytes and calls the provided emitter for each byte.
 */
fun stringToUTF8(str: String, emitter: (Byte) -> Unit) {
    val bytes = str.toByteArray(Charsets.UTF_8)
    for (byte in bytes) {
        emitter(byte)
    }
}

/**
 * Counts the number of consecutive 1 bits starting from the most-significant bit in an 8‑bit value.
 */
fun countLeadingOnes(byte: Int): Int {
    var count = 0
    for (i in 7 downTo 0) {
        if (((byte shr i) and 1) == 1) count++ else break
    }
    return count
}

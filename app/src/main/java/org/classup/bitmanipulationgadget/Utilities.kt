package org.classup.bitmanipulationgadget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import org.classup.bitmanipulationgadget.ui.theme.BMGPageIndicator
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import java.util.Locale

const val INVALID_TEXT = "Invalid input!"

fun inputTo64Binary(input: String): String {
    // Enforces 64 bits limit.
    var result = INVALID_TEXT

    when {
        input.lowercase(Locale.ENGLISH).startsWith("0b") -> {
            var binaryString = input.substring(2)

            if (binaryString.isNotEmpty()) {
                if (binaryString.all { it == '0' }) {
                    result = "0"
                }
                else if (binaryString.all { it == '0' || it == '1' }) {
                    binaryString = binaryString.trimStart('0')
                    if ( binaryString.length < 65) {
                        result = binaryString
                    }
                }
            }
        }
        input.lowercase(Locale.ENGLISH).startsWith("0x") -> {
            try {
                val hexString = input.substring(2)
                val regexPattern = "[a-f0-9A-F]+".toRegex()  // Disallow arithmetic signs.

                if (hexString.length < 17 && regexPattern.matches(hexString)) {
                    val hexValue = hexString.toULong(16)
                    result = hexValue.toString(2)
                }
            } catch (_: Exception) {}
        }
        else -> {
            try {
                val decimalValue = input.toLong()
                result = java.lang.Long.toBinaryString(decimalValue)
            } catch (_: Exception) {}
        }
    }

    return result
}

fun bmgTextIsValid(bmgText: String): Boolean {
    return bmgText != INVALID_TEXT
}

fun spaceEvery4th(string: String): String {
    return string.replace(" ", "").chunked(4).joinToString(" ")
}

fun padBinary16Divisible(binaryString: String): String {
    val padding = if (binaryString.length % 16 > 0) 16 - binaryString.length % 16 else 0
    return "0".repeat(padding) + binaryString
}

@Composable
fun DebugHighlightParent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 12.dp, color = Color.Red) // Adjust width and color as needed
            .background(Color.Transparent) // Optional: Set background color
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator (pages: Int, pagerState: PagerState) {
    if (pages < 2) { return }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    )
    {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) BMGText else BMGPageIndicator
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)
            )
        }
    }
}
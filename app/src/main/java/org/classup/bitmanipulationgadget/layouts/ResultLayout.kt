package org.classup.bitmanipulationgadget.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.bmgStringIsValid
import org.classup.bitmanipulationgadget.padBinary16Divisible
import org.classup.bitmanipulationgadget.spaceEvery4th
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import kotlin.math.ceil

@Composable
fun ResultLayout(resultBinary: String) {
    BinaryResult(resultBinary)
    DecimalResult(resultBinary)
    HexResult(resultBinary)
}

@Composable
private fun BinaryResult(result: String) {
    val resultPadded = padBinary16Divisible(result)

    val padBits = "0000 0000 0000 0000"

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(11.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        val coloredResult = buildAnnotatedString {
            val rowCount = if (bmgStringIsValid(result)) ceil((resultPadded.length / 16).toDouble()).toInt() else 0

            if (rowCount < 1) {
                withStyle(style = SpanStyle(color = BMGText)) {
                    append("BINARY RESULT")
                }
            }
            else {
                // Do the padding first.
                for (i in 0 until 4 - rowCount) {
                    withStyle(style = SpanStyle(color = BMGText)) {
                        append(padBits + if (i == 3) "" else "\n")
                    }
                }

                // Then, draw the answer after.
                for (i in 0 until rowCount) {
                    withStyle(style = SpanStyle(color = BMGTextBrighter)) {
                        append(
                            spaceEvery4th(
                                resultPadded.substring(
                                    i * 16,
                                    i * 16 + 16
                                )
                            ) + if (i == rowCount - 1) "" else "\n"
                        )
                    }
                }
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = coloredResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

@Composable
private fun DecimalResult(result: String) {
    val decimalResult: String = if (bmgStringIsValid(result)) {
        result.toULong(2).toLong().toString()
    }
    else {
        "DECIMAL RESULT"
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(11.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = decimalResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

@Composable
private fun HexResult(result: String) {
    var hexResult: String

    hexResult = if (bmgStringIsValid(result)) {
        "0x" + result.toULong(2).toString(16)
    }
    else {
        "HEXADECIMAL RESULT"
    }

    if (hexResult == "0x") hexResult = "0x0"  // Lol

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(11.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = hexResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
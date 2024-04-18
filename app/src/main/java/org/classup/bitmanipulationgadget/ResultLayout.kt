package org.classup.bitmanipulationgadget

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import kotlin.math.ceil

@Composable
fun ResultLayout(result: String) {
    Column {
        BinaryResult(result)
        DecimalResult(result)
        HexResult(result)
    }
}

@Composable
private fun BinaryResult(result: String) {
    // How the final text is achieved is so moronic but works.
    val padBits = "0000 0000 0000 0000"
    val reversedResult = result.reversed()

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        val coloredResult = buildAnnotatedString {
            val rowCount = ceil((result.length / 16).toDouble())

            for (i in 3 downTo 0) {
                val color: Color = if (i % 2 == 0) BMGTextBrighter else BMGText

                withStyle(style = SpanStyle(color = color)) {
                    append((if (i < rowCount) spaceEvery4th(reversedResult.substring(i * 16, i * 16 + 16).reversed()) else padBits) + "\n")
                }
            }
        }

        Text(
            text = coloredResult,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DecimalResult(result: String) {
    val decimalResult: String = if (bmgTextIsValid(result)) {
        result.toBigInteger(2).toString()
    }
    else {
        "DECIMAL RESULT"
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Text(text = decimalResult, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp))
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun HexResult(result: String) {
    var hexResult: String

    hexResult = if (bmgTextIsValid(result)) {
        "0x" + result.toBigInteger(2).toString(16)
    }
    else {
        "HEXADECIMAL RESULT"
    }

    if (hexResult == "0x") hexResult = "0x0"
    
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Text(text = hexResult, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp))
    }
}
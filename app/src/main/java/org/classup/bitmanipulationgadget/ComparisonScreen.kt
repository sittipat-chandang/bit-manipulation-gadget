package org.classup.bitmanipulationgadget

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.kufam
import java.math.BigInteger

// I don't know what I'm doing. Probably a lot of weird/hacky code.

private fun bitwiseCompare(operation: BitwiseOperation, first: String, second: String): String {
    val operableFirst: BigInteger
    val operableSecond: BigInteger

    try {
        /* Can't use Long here because the parser doesn't treat the first bit as a sign bit.
           This means that 1111111111111111111111111111111111111111111111111111111111111111
           will be equal to 01111111111111111111111111111111111111111111111111111111111111111,
           which adds up to 65 bits.
        */
        operableFirst = first.toBigInteger(2)
        operableSecond = second.toBigInteger(2)
    } catch (e: Exception) {
        return INVALID_TEXT
    }

    return when (operation) {
        BitwiseOperation.AND -> {
            convertToBinary((operableFirst and operableSecond).toString())
        }
        BitwiseOperation.OR -> {
            convertToBinary((operableFirst or operableSecond).toString())
        }
        BitwiseOperation.XOR -> {
            convertToBinary((operableFirst xor operableSecond).toString())
        }
        else -> {
            INVALID_TEXT
        }
    }
}

@Composable
fun ComparisonScreen(operation: BitwiseOperation, first: String, second: String, updateInputs: (String, String) -> Unit)
{
    // This screen propagates input updates to MainActivity.
    var firstBinary = convertToBinary(first)
    var secondBinary = convertToBinary(second)
    var result = bitwiseCompare(operation, firstBinary, secondBinary)

    var pages = 1

    if (isValid(firstBinary) && (firstBinary.length > secondBinary.length || !isValid(secondBinary))) {
        val padding = if (firstBinary.length % 16 > 0) 16 - firstBinary.length % 16 else 0
        firstBinary = "0".repeat(padding) + firstBinary

        if (isValid(secondBinary)) {
            secondBinary = "0".repeat(firstBinary.length - secondBinary.length) + secondBinary
            result = "0".repeat(firstBinary.length - result.length) + result
        }

        pages = firstBinary.length / 16
    }
    else if (isValid(secondBinary)) {
        val padding = if (secondBinary.length % 16 > 0) 16 - secondBinary.length % 16 else 0
        secondBinary = "0".repeat(padding) + secondBinary

        if (isValid(firstBinary)) {
            firstBinary = "0".repeat(secondBinary.length - firstBinary.length) + firstBinary
            result = "0".repeat(secondBinary.length - result.length) + result
        }

        pages = secondBinary.length / 16
    }

    Column {
        InputCard(operation, first, second) {newFirst, newSecond ->
            updateInputs(newFirst, newSecond)
        }
        SolutionCard(operation, firstBinary, secondBinary, result, pages)
        ResultLayout(result)
    }
}

@Composable
private fun InputCard(operation: BitwiseOperation, first: String, second:String, updateInputs: (String, String) -> Unit) {
    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    val textFieldTextStyle = TextStyle(fontSize = 24.sp, fontFamily = kufam, color = Color.Black)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            TextField(
                value = first,
                textStyle = textFieldTextStyle,
                onValueChange = {updateInputs(it, second)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 12.dp, bottom = 12.dp, top = 12.dp)
            )
            Text(
                text = operation.name,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            )
            TextField(
                value = second,
                textStyle = textFieldTextStyle,
                onValueChange = {updateInputs(first, it)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 12.dp, bottom = 12.dp, top = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SolutionCard(operation: BitwiseOperation, firstBinary: String, secondBinary: String, result: String, pages: Int) {
    val pagerState = rememberPagerState(pageCount = { pages })

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        HorizontalPager(state = pagerState) {page ->
            Column {
                Text(
                    text = if (isValid(firstBinary)) firstBinary.substring(16 * page, 16 * (page + 1)) else "FIRST INPUT",
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Text(text = operation.name, fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(
                    text = if (isValid(secondBinary)) secondBinary.substring(16 * page, 16 * (page + 1)) else "SECOND INPUT",
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    // TODO: Color coding
                    text = if (isValid(result)) result.substring(16 * page, 16 * (page + 1)) else "RESULT",
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
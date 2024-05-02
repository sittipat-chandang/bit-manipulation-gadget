package org.classup.bitmanipulationgadget.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.INVALID_TEXT
import org.classup.bitmanipulationgadget.bmgStringIsValid
import org.classup.bitmanipulationgadget.inputTo64Binary
import org.classup.bitmanipulationgadget.layouts.PageIndicatorLayout
import org.classup.bitmanipulationgadget.layouts.ResultLayout
import org.classup.bitmanipulationgadget.padBinary16Divisible
import org.classup.bitmanipulationgadget.spaceEvery4th
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGTextUnfocused
import org.classup.bitmanipulationgadget.ui.theme.kufam

// I don't know what I'm doing. Probably a lot of weird/hacky code.

@Composable
fun ComparisonScreen(operation: BitwiseComparisonOperation, first: String, second: String, rememberInputs: (String, String) -> Unit)
{
    // This screen propagates input updates to MainActivity.
    val firstBinary = inputTo64Binary(first)
    val secondBinary = inputTo64Binary(second)
    val result = bitwiseCompare(operation, firstBinary, secondBinary)

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    )
    {
        InputCard(operation, first, second) {newFirst, newSecond ->
            rememberInputs(newFirst, newSecond)
        }
        SolutionCard(operation, firstBinary, secondBinary, result)
        ResultLayout(result)
    }
}

private fun bitwiseCompare(operation: BitwiseComparisonOperation, firstBinary: String, secondBinary: String): String {
    val operableFirst: ULong
    val operableSecond: ULong

    try {
        /* Can't use Long here because the parser doesn't treat the first bit as a sign bit.
           This means that 1111111111111111111111111111111111111111111111111111111111111111
           will be equal to 01111111111111111111111111111111111111111111111111111111111111111,
           which adds up to 65 bits.
        */
        operableFirst = firstBinary.toULong(2)
        operableSecond = secondBinary.toULong(2)
    } catch (e: Exception) {
        return INVALID_TEXT
    }

    return when (operation) {
        BitwiseComparisonOperation.AND -> {
            (operableFirst and operableSecond).toString(2)
        }
        BitwiseComparisonOperation.OR -> {
            (operableFirst or operableSecond).toString(2)
        }
        BitwiseComparisonOperation.XOR -> {
            (operableFirst xor operableSecond).toString(2)
        }
    }
}

@Composable
private fun InputCard(operation: BitwiseComparisonOperation, first: String, second:String, rememberInputs: (String, String) -> Unit) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedBorderColor = BMGText,
        focusedBorderColor = BMGTextBrighter
    )
    val textFieldTextStyle = TextStyle(fontSize = 24.sp, fontFamily = kufam, color = Color.Black, textAlign = TextAlign.Center)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth())
        {
            OutlinedTextField(
                value = first,
                textStyle = textFieldTextStyle,
                onValueChange = { rememberInputs(it, second) },
                label = { Text("First input", fontSize = 16.sp, color = BMGTextUnfocused) },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 3.dp)
            )
            Text(
                text = operation.name,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                value = second,
                textStyle = textFieldTextStyle,
                onValueChange = { rememberInputs(first, it) },
                label = { Text("Second input", fontSize = 16.sp, color = BMGTextUnfocused) },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SolutionCard(operation: BitwiseComparisonOperation, firstBinary: String, secondBinary: String, result: String) {
    var pages = 1

    var firstBinaryPadded = firstBinary
    var secondBinaryPadded = secondBinary
    var resultPadded = result

    if (bmgStringIsValid(firstBinary) && (firstBinary.length > secondBinary.length || !bmgStringIsValid(secondBinary))) {
        firstBinaryPadded = padBinary16Divisible(firstBinary)

        if (bmgStringIsValid(secondBinary)) {
            secondBinaryPadded = "0".repeat(firstBinaryPadded.length - secondBinary.length) + secondBinary
            resultPadded = "0".repeat(firstBinaryPadded.length - result.length) + result
        }

        pages = firstBinaryPadded.length / 16
    }
    else if (bmgStringIsValid(secondBinary)) {
        secondBinaryPadded = padBinary16Divisible(secondBinary)

        if (bmgStringIsValid(firstBinary)) {
            firstBinaryPadded = "0".repeat(secondBinaryPadded.length - firstBinary.length) + firstBinary
            resultPadded = "0".repeat(secondBinaryPadded.length - result.length) + result
        }

        pages = secondBinaryPadded.length / 16
        Log.d("Fart", "$firstBinary | $secondBinaryPadded | $resultPadded")
    }

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
            val formattedResult: AnnotatedString = if (bmgStringIsValid(resultPadded)) {
                val spacedResult = spaceEvery4th(resultPadded.substring(16 * page, 16 * (page + 1)))

                buildAnnotatedString {
                    for (i in spacedResult.indices) {
                        withStyle(style = SpanStyle(color = if (spacedResult[i] == '1') Color.Green else Color.Red)) {
                            append(spacedResult[i])
                        }
                    }
                }
            } else {
                buildAnnotatedString { append("RESULT") }
            }

            Column {
                Text(
                    text = if (bmgStringIsValid(firstBinaryPadded)) spaceEvery4th(firstBinaryPadded.substring(16 * page, 16 * (page + 1))) else "FIRST INPUT",
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 9.dp)
                )
                Text(text = operation.name, fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(
                    text = if (bmgStringIsValid(secondBinaryPadded)) spaceEvery4th(secondBinaryPadded.substring(16 * page, 16 * (page + 1))) else "SECOND INPUT",
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = formattedResult,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        PageIndicatorLayout(pages, pagerState)
    }
}
package org.classup.bitmanipulationgadget.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.core.math.MathUtils.clamp
import org.classup.bitmanipulationgadget.INVALID_TEXT
import org.classup.bitmanipulationgadget.bmgStringIsValid
import org.classup.bitmanipulationgadget.inputTo64Binary
import org.classup.bitmanipulationgadget.layouts.PageIndicatorLayout
import org.classup.bitmanipulationgadget.layouts.ResultLayout
import org.classup.bitmanipulationgadget.padBinary64
import org.classup.bitmanipulationgadget.spaceEvery4th
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGTextUnfocused
import org.classup.bitmanipulationgadget.ui.theme.kufam

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShiftScreen(input: String, shiftCount: String, rememberInputs: (String, String) -> Unit) {
    val inputBinary = inputTo64Binary(input)
    var resultBinary = INVALID_TEXT
    val checkedShiftCount = try {
        val shiftCountFormatted = shiftCount.toInt()
        if (shiftCountFormatted in 1..64) shiftCountFormatted else 0
    } catch (_: Exception) {
        0
    }

    val pagerState = rememberPagerState(pageCount = { 3 })

    if (bmgStringIsValid(inputBinary) && checkedShiftCount > 0) {
        when (pagerState.currentPage) {
            0 -> resultBinary = inputBinary.toULong(2)
                .shl(checkedShiftCount)
                .toString(2)

            1 -> resultBinary = inputBinary.toULong(2)
                .shr(checkedShiftCount)
                .toString(2)

            2 -> resultBinary = if (inputBinary.length == 64 && inputBinary[0] == '1') {
                val resultBinaryLogical = padBinary64(
                    inputBinary.toULong(2)
                        .shr(checkedShiftCount)
                        .toString(2)
                )
                "1".repeat(checkedShiftCount) + resultBinaryLogical.substring(
                    clamp(
                        checkedShiftCount,
                        0,
                        63
                    ), 64
                )
            } else {
                inputBinary.toULong(2)
                    .shr(checkedShiftCount)
                    .toString(2)
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()))
    {
        InputCard(pagerState, input, shiftCount) { newInput, newShiftCount ->
            rememberInputs(newInput, newShiftCount)
        }
        SolutionCard(BitwiseShiftType.entries[pagerState.currentPage], inputBinary, checkedShiftCount, resultBinary)
        ResultLayout(resultBinary)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InputCard(pagerState: PagerState, input: String, shiftCount: String, rememberInputs: (String, String) -> Unit) {
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
        HorizontalPager(state = pagerState) {page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            )
            {
                OutlinedTextField(
                    value = input,
                    textStyle = textFieldTextStyle,
                    onValueChange = { rememberInputs(it, shiftCount) },
                    label = { Text("Input", fontSize = 16.sp, color = BMGTextUnfocused) },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 3.dp)
                )
                Text(
                    text = "SHIFT " + BitwiseShiftType.entries[pagerState.currentPage].name.replace("_", " ") + " BY",
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = shiftCount,
                    textStyle = textFieldTextStyle,
                    onValueChange = { rememberInputs(input, it) },
                    label = { Text("Shift count (0-64)", fontSize = 16.sp, color = BMGTextUnfocused) },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SolutionCard(shiftType: BitwiseShiftType, inputBinary: String, shiftCount: Int, resultBinary: String) {
    val inputBinaryPadded = if (bmgStringIsValid(inputBinary)) padBinary64(inputBinary) else INVALID_TEXT
    val resultPadded = if (bmgStringIsValid(resultBinary)) padBinary64(resultBinary) else INVALID_TEXT

    val pages = if (bmgStringIsValid(inputBinaryPadded)) inputBinaryPadded.length / 16 else 1

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
                val resultPartition = resultPadded.substring(16 * page, 16 * (page + 1))

                buildAnnotatedString {
                    for (i in resultPartition.indices) {
                        if (shiftType == BitwiseShiftType.LEFT) {
                            withStyle(style = SpanStyle(color = if (i + 16 * page >= 64 - shiftCount) Color.Blue else Color.Black)) {
                                append(if (i % 4 == 0) " " + resultPartition[i].toString() else resultPartition[i].toString())
                            }
                        }
                        else {
                            withStyle(style = SpanStyle(color = if (i + 16 * page < shiftCount) Color.Blue else Color.Black)) {
                                append(if (i % 4 == 0) " " + resultPartition[i].toString() else resultPartition[i].toString())
                            }
                        }
                    }
                }
            } else {
                buildAnnotatedString { append("RESULT") }
            }

            Column {
                Text(
                    text = if (bmgStringIsValid(inputBinaryPadded)) spaceEvery4th(inputBinaryPadded.substring(16 * page, 16 * (page + 1))) else "FIRST INPUT",
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 9.dp)
                )
                Text(text = "SHIFT " + shiftType.name.replace('_', ' ') + " BY", fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(
                    text = if (shiftCount > 0) shiftCount.toString() else "SHIFT COUNT",
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
package org.classup.bitmanipulationgadget.screens

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.inputTo64Binary
import org.classup.bitmanipulationgadget.layouts.PageIndicatorLayout
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGTextUnfocused
import org.classup.bitmanipulationgadget.ui.theme.kufam

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShiftScreen(input: String, shiftCount: String, rememberInputs: (String, String) -> Unit) {
    val inputBinary = inputTo64Binary(input)
    val pagerState = rememberPagerState(pageCount = { 3 })

    Log.d("Fart", pagerState.currentPage.toString())

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()))
    {
        InputCard(pagerState, input, shiftCount) { newInput, newShiftCount ->
            rememberInputs(newInput, newShiftCount)
        }
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
                    label = { Text("Shift count", fontSize = 16.sp, color = BMGTextUnfocused) },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                )
            }
        }
    }
}
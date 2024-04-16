package org.classup.bitmanipulationgadget

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
import kotlin.math.ceil

// I don't know what I'm doing. Probably a lot of weird/hacky code.

private fun calcCompare(operation: BitwiseOperation, first: String, second: String): String {
    val operableFirst: Long
    val operableSecond: Long

    try {
        // Convert input(s) to binary first then to long so we can do comparison.
        operableFirst = first.toLong(2)
        operableSecond = second.toLong(2)
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
    val firstBinary = convertToBinary(first)
    val secondBinary = convertToBinary(second)

    val result = calcCompare(operation, firstBinary, secondBinary)

    Column {
        InputCard(operation, first, second) {newFirst, newSecond ->
            updateInputs(newFirst, newSecond)
        }
        SolutionCard(operation, firstBinary, secondBinary, result)
    }
}

@Composable
fun InputCard(operation: BitwiseOperation, first: String, second:String, updateInputs: (String, String) -> Unit) {
    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    val textFieldTextstyle = TextStyle(fontSize = 24.sp, fontFamily = kufam, color = Color.Black)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            TextField(
                value = first,
                textStyle = textFieldTextstyle,
                onValueChange = {updateInputs(it, second)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start=12.dp, bottom = 12.dp, top=12.dp)
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
                textStyle = textFieldTextstyle,
                onValueChange = {updateInputs(first, it)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end=12.dp, bottom = 12.dp, top=12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SolutionCard(operation: BitwiseOperation, firstBinary: String, secondBinary: String, result: String) {
    // TODO: Paginate

    val pagerState = rememberPagerState(pageCount = {
        val longestInput = if (firstBinary.length > secondBinary.length) firstBinary.length else secondBinary.length
        ceil((longestInput / 8).toDouble()).toInt()
    })

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        HorizontalPager(state = pagerState) {page ->
            Column {
                Text(
                    text = firstBinary,
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = operation.name, fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(
                    text = secondBinary,
                    fontSize = 26.sp,
                    color= Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = result, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
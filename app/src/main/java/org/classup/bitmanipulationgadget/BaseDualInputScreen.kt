package org.classup.bitmanipulationgadget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.kufam

// I don't know what I'm doing. Probably a lot of weird/hacky code.

@Composable
fun BaseDualInputScreen(operation: BitwiseOperation) {
    var first by remember {mutableStateOf("")}
    var second by remember { mutableStateOf("")}

    Column {
        InputCard(operation, first, second) {newFirst, newSecond ->
            first = newFirst
            second = newSecond
        }
        SolutionCard(operation, first, second)
    }
}

@Composable
fun InputCard(operation: BitwiseOperation, first: String, second:String, updateShared: (String, String) -> Unit) {
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
                onValueChange = {updateShared(it, second)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            Text(
                text = operation.name,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            )
            TextField(
                value = second,
                textStyle = textFieldTextstyle,
                onValueChange = {updateShared(first, it)},
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun SolutionCard(operation: BitwiseOperation, first: String, second: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Column {
            Text(text = first, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(text = operation.name, fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(text = second, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}
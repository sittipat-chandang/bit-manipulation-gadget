package org.classup.bitmanipulationgadget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter

@Composable
fun ResultLayout(result: String) {
    Column {
        DecimalResult(result)
        HexResult(result)
    }
}

@Composable
private fun DecimalResult(result: String) {
    var decimalResult = result
    decimalResult = if (isValid(result)) {
        result.toLong(2).toString()
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
    var hexResult = result

    hexResult = if (isValid(result)) {
        "0x" + result.toLong(2).toHexString().trimStart('0')
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
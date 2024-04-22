package org.classup.bitmanipulationgadget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.classup.bitmanipulationgadget.navigation.Destinations
import org.classup.bitmanipulationgadget.navigation.NavItemInfo
import org.classup.bitmanipulationgadget.screens.ComparisonScreen
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BitManipulationGadgetTheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitManipulationGadgetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                )
                {
                   NavBar()
                }
            }
        }
    }
}

@Composable
fun NavBar() {
    val navController = rememberNavController()
    var navSelectedItem by remember {
        mutableIntStateOf(0)
    }

    var firstAnd by remember {mutableStateOf("")}
    var secondAnd by remember {mutableStateOf("")}
    var firstOr by remember {mutableStateOf("")}
    var secondOr by remember {mutableStateOf("")}
    var firstXor by remember {mutableStateOf("")}
    var secondXor by remember {mutableStateOf("")}

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                NavItemInfo().getAllNavItems().forEachIndexed {
                    index, itemInfo -> NavigationBarItem(
                        selected = index == navSelectedItem,
                        onClick = {
                            navSelectedItem = index
                            navController.navigate(itemInfo.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {Icon(imageVector = itemInfo.icon, contentDescription = itemInfo.label, tint = BMGText, modifier = Modifier.size(16.dp))},
                        label = {Text(text = itemInfo.label, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)},
                        colors = NavigationBarItemDefaults.colors(
                            // No idea what part of a theme actually changes the indicator so this will do.
                            indicatorColor = BMGOrangeBrighter
                        )
                    )
                }
            }
        }
    )
    {
        paddingValues -> NavHost(
            navController = navController,
            startDestination = Destinations.And.route,
            modifier = Modifier.padding(paddingValues)
        )
        {
            composable(route = Destinations.And.route) {
                ComparisonScreen(BitwiseOperation.AND, firstAnd, secondAnd) {newFirst, newSecond ->
                    firstAnd = newFirst
                    secondAnd = newSecond
               }
            }
            composable(route = Destinations.Or.route) {
                ComparisonScreen(BitwiseOperation.OR, firstOr, secondOr) {newFirst, newSecond ->
                    firstOr = newFirst
                    secondOr = newSecond
                }
            }
            composable(route = Destinations.Xor.route) {
                ComparisonScreen(BitwiseOperation.XOR, firstXor, secondXor) {newFirst, newSecond ->
                    firstXor = newFirst
                    secondXor = newSecond
                }
            }
            composable(route = Destinations.Complement.route) {}
            composable(route = Destinations.Shift.route) {}
        }
    }
}
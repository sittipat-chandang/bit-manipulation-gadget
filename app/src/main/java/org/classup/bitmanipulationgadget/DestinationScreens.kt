package org.classup.bitmanipulationgadget

sealed class DestinationScreens(val route: String) {
    // TODO: Does data object cause any problems? If so, fallback to just using object.
    data object And: DestinationScreens("and")
    data object Or: DestinationScreens("or")
    data object Xor: DestinationScreens("xor")
    data object Complement: DestinationScreens("complement")
    data object Shift: DestinationScreens("shift")
}
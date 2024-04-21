package org.classup.bitmanipulationgadget

sealed class Destinations(val route: String) {
    data object And: Destinations("and")
    data object Or: Destinations("or")
    data object Xor: Destinations("xor")
    data object Complement: Destinations("complement")
    data object Shift: Destinations("shift")
}
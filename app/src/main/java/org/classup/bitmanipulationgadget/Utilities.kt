package org.classup.bitmanipulationgadget

fun convertInputToBinary(input: String): String {
    return when {
        input.startsWith("0b") -> {
            val binaryValue = input.substring(2)

            if (binaryValue.all {it == '0' || it == '1'} && binaryValue.length < 65) {
                binaryValue
            }
            else {
                "Invalid input!"
            }
        }
        input.startsWith("0x") -> {
            try {
                val hexString = input.substring(2)
                if (hexString.length > 16) {
                    "Invalid input!"
                }
                else {
                    val hexValue = hexString.toBigInteger(16)
                    val binaryValue = hexValue.toString(2)
                    binaryValue
                }
            } catch (e: Exception) {
                "Invalid input!"
            }
        }
        else -> {
            try {
                val decimalValue = input.toLong()
                val binaryValue = java.lang.Long.toBinaryString(decimalValue)
                binaryValue
            } catch (e: Exception) {
                "Invalid input!"
            }
        }
    }
}
package com.mivuelto.core

fun Int.isOdd(): Boolean = this % 2 != 0

fun String.toMaskedRange(start: Int = 0, end: Int): String {
    if (this.length <= end) return this
    val mask = "*".repeat(end - start)
    return this.substring(0, start) + mask + this.substring(end)
}

fun String.formatDate(): String = this // Simplificado para el ejemplo

fun String.addCommas(): String {
    return try {
        "%,d".format(this.toLong())
    } catch (e: Exception) {
        this
    }
}

fun Double.addCommas(): String {
    return "%,.2f".format(this)
}

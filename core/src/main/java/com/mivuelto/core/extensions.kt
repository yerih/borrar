package com.mivuelto.core

import android.util.Log

fun Int.isOdd(): Boolean = this % 2 != 0

fun String.toMaskedRange(start: Int = 0, end: Int): String {
    if (this.length <= end) return this
    val mask = "*".repeat(end - start)
    return this.substring(0, start) + mask + this.substring(end)
}

fun String.formatDate(): String = this // Simplificado para el ejemplo
fun String.formatTime(): String = this // Simplificado para el ejemplo

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


fun String.checkAmount(): String = if(isEmpty()) String.format("%.2f",0.00).replace(".",",") else if(!contains(",")) String.format("%.2f",toDouble()/100).replace(".",",") else this

fun String.lengthGreaterThan(value: Int): Boolean = replace(".", "").replace(",", "").length > value

fun String.toPhoneFormat(): String {
    val digits = filter(Char::isDigit)

    if (digits.length != 11) return this

    return "(${digits.substring(0, 4)}) ${digits.substring(4, 7)} ${digits.substring(7, 9)} ${digits.substring(9, 11)}"
}

fun Any.log(msg: String = "", tag: String = "TGB") = Log.i(tag, msg)

fun String.isGreaterThanZero(): Boolean {
    val value = toDoubleOrNull() ?: return false
    return value > 0
}


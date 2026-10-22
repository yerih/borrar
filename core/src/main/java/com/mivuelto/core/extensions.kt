package com.mivuelto.core

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale

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

/**
 * Parser único y tolerante para las fechas ISO del backend:
 * acepta el formato de la spec (`2026-09-24T18:03:11`) y segundos con
 * fracción de longitud variable (`2026-10-22T07:53:34.330994`).
 */
private val API_DATE_TIME_PARSER: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
    .toFormatter(Locale.ENGLISH)

private val DATE_OUTPUT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
private val TIME_OUTPUT: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
private val DATE_TIME_OUTPUT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH)

private fun String.parseApiDateTime(): LocalDateTime = LocalDateTime.parse(this, API_DATE_TIME_PARSER)

/** `2026-09-24T18:03:11` -> `24/09/2026` */
fun String.toDateFormatted(): String = parseApiDateTime().format(DATE_OUTPUT)

/** `2026-09-24T18:03:11` -> `6:03 pm` */
fun String.toTimeFormatted(): String = parseApiDateTime().format(TIME_OUTPUT).lowercase(Locale.ENGLISH)

/** `2026-09-24T18:03:11` -> `24/09/2026 06:03 pm` */
fun LocalDateTime.toDateTimeFormatted(): String = format(DATE_TIME_OUTPUT).lowercase(Locale.ENGLISH)

/** `2026-09-24T18:03:11` -> `24/09/2026 06:03 pm` */
fun String.toDateTimeFormatted(): String = parseApiDateTime().toDateTimeFormatted()

fun Long.formatCents(): String {
    val units = this / 100
    val remainder = (this % 100).toString().padStart(2, '0')
    return "$units,$remainder"
}

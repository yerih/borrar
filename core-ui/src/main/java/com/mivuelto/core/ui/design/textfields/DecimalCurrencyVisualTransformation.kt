package com.mivuelto.core.ui.design.textfields

import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class DecimalCurrencyVisualTransformation(private val prefix: String) : VisualTransformation {
    private val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    private val formatter = DecimalFormat("#,##0.00", symbols)

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        if (originalText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val number = originalText.toDouble() / 100.0
        val formattedNumber = formatter.format(number)

        val out = prefix + formattedNumber

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return 0
                var digitsCount = 0
                var transformedOffset = prefix.length

                while (digitsCount < offset && transformedOffset < out.length) {
                    if (out[transformedOffset].isDigit()) {
                        digitsCount++
                    }
                    transformedOffset++
                }
                if (offset == originalText.length) {
                    return out.length
                }

                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= prefix.length) return 0
                var digitsCount = 0
                val end = offset.coerceAtMost(out.length)
                for (i in prefix.length until end) {
                    if (out[i].isDigit()) {
                        digitsCount++
                    }
                }

                return digitsCount.coerceIn(0, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}



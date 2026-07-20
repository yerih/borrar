package com.mivuelto.core.ui.design.textfields

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(11)

        val out = StringBuilder()
        val originalToTransformed = IntArray(digits.length + 1)

        var transformedIndex = 0

        digits.forEachIndexed { index, c ->

            if (index == 0) {
                out.append("(")
                transformedIndex++
            }

            originalToTransformed[index] = transformedIndex

            out.append(c)
            transformedIndex++

            when (index) {
                3 -> {
                    out.append(") ")
                    transformedIndex += 2
                }
                6, 8 -> {
                    out.append(" ")
                    transformedIndex++
                }
            }
        }

        originalToTransformed[digits.length] = transformedIndex

        val transformedToOriginal = IntArray(out.length + 1)

        var original = 0
        for (i in transformedToOriginal.indices) {
            while (
                original < originalToTransformed.lastIndex &&
                originalToTransformed[original + 1] <= i
            ) {
                original++
            }
            transformedToOriginal[i] = original
        }

        return TransformedText(
            AnnotatedString(out.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int) =
                    originalToTransformed[offset.coerceIn(0, digits.length)]

                override fun transformedToOriginal(offset: Int) =
                    transformedToOriginal[offset.coerceIn(0, out.length)]
            }
        )
    }
}
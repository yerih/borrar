package com.mivuelto.core.ui.design.textfields

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class DecimalCurrencyVisualTransformationTest {

    private val transformation = DecimalCurrencyVisualTransformation("Bs. ")

    private fun filter(raw: String): String =
        transformation.filter(AnnotatedString(raw)).text.text

    @Test
    fun `empty input shows Bs 0,00`() {
        assertEquals("Bs. 0,00", filter(""))
    }

    @Test
    fun `zero input shows Bs 0,00`() {
        assertEquals("Bs. 0,00", filter("0"))
        assertEquals("Bs. 0,00", filter("00"))
        assertEquals("Bs. 0,00", filter("000"))
    }

    @Test
    fun `formats cents with comma decimals`() {
        assertEquals("Bs. 0,05", filter("5"))
        assertEquals("Bs. 100,00", filter("10000"))
        assertEquals("Bs. 150,50", filter("15050"))
    }

    @Test
    fun `groups thousands with dots`() {
        assertEquals("Bs. 1.000,00", filter("100000"))
        assertEquals("Bs. 1.234.567,89", filter("123456789"))
    }

    @Test
    fun `empty mapping keeps the caret at the end of the placeholder`() {
        val transformed = transformation.filter(AnnotatedString(""))
        val placeholder = "Bs. 0,00"
        assertEquals(placeholder, transformed.text.text)
        assertEquals(placeholder.length, transformed.offsetMapping.originalToTransformed(0))
        assertEquals(0, transformed.offsetMapping.transformedToOriginal(0))
        assertEquals(
            0,
            transformed.offsetMapping.transformedToOriginal(placeholder.length)
        )
    }

    @Test
    fun `offset mapping round trips for a real amount`() {
        val transformed = transformation.filter(AnnotatedString("15050"))
        val mapping = transformed.offsetMapping
        assertEquals(0, mapping.originalToTransformed(0))
        assertEquals(transformed.text.text.length, mapping.originalToTransformed(5))
        assertEquals(5, mapping.transformedToOriginal(transformed.text.text.length))
        assertEquals(0, mapping.transformedToOriginal(0))
        assertEquals(0, mapping.transformedToOriginal("Bs. ".length))
    }
}

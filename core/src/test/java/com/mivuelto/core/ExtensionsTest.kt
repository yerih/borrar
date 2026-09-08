package com.mivuelto.core

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `isOdd returns true for odd numbers`() {
        assertEquals(true, 1.isOdd())
        assertEquals(true, 3.isOdd())
        assertEquals(true, 99.isOdd())
        assertEquals(true, (-5).isOdd())
    }

    @Test
    fun `isOdd returns false for even numbers`() {
        assertEquals(false, 0.isOdd())
        assertEquals(false, 2.isOdd())
        assertEquals(false, 100.isOdd())
        assertEquals(false, (-4).isOdd())
    }

    @Test
    fun `toMaskedRange masks middle portion of string`() {
        assertEquals("ab****gh", "abcdefgh".toMaskedRange(2, 6))
        assertEquals("12****7890", "1234567890".toMaskedRange(2, 6))
    }

    @Test
    fun `toMaskedRange returns original when length is less than or equal to end`() {
        assertEquals("abc", "abc".toMaskedRange(0, 3))
        assertEquals("ab", "ab".toMaskedRange(0, 5))
    }

    @Test
    fun `addCommas on Long string formats with thousands separator`() {
        assertEquals("1.000", "1000".addCommas())
        assertEquals("1.000.000", "1000000".addCommas())
        assertEquals("100", "100".addCommas())
    }

    @Test
    fun `addCommas on Long string returns original on parse failure`() {
        assertEquals("abc", "abc".addCommas())
        assertEquals("", "".addCommas())
    }

    @Test
    fun `addCommas on Double formats with thousands and 2 decimal places`() {
        assertEquals("1.000,00", 1000.0.addCommas())
        assertEquals("1.234.567,89", 1234567.89.addCommas())
    }

    @Test
    fun `checkAmount converts cents to decimal format with comma`() {
        assertEquals("1,00", "100".checkAmount())
        assertEquals("10,00", "1000".checkAmount())
        assertEquals("100,00", "10000".checkAmount())
        assertEquals("1234,56", "123456".checkAmount())
    }

    @Test
    fun `checkAmount returns zero formatted when empty`() {
        assertEquals("0,00", "".checkAmount())
    }

    @Test
    fun `checkAmount returns original when already contains comma`() {
        assertEquals("100,50", "100,50".checkAmount())
        assertEquals("1.234,56", "1.234,56".checkAmount())
    }

    @Test
    fun `toPhoneFormat formats 11 digits correctly`() {
        assertEquals("(0412) 123 45 67", "04121234567".toPhoneFormat())
        assertEquals("(0424) 987 65 43", "04249876543".toPhoneFormat())
    }

    @Test
    fun `toPhoneFormat returns original when not 11 digits`() {
        assertEquals("12345", "12345".toPhoneFormat())
        assertEquals("123456789012", "123456789012".toPhoneFormat())
        assertEquals("", "".toPhoneFormat())
    }

    @Test
    fun `toPhoneFormat ignores non-digit characters`() {
        assertEquals("(0412) 123 45 67", "0412-123-45-67".toPhoneFormat())
        assertEquals("(0412) 123 45 67", "(0412) 123 45 67".toPhoneFormat())
    }

    @Test
    fun `formatDate returns unchanged (simplified)`() {
        assertEquals("0101", "0101".formatDate())
        assertEquals("22/06/2026", "22/06/2026".formatDate())
    }

    @Test
    fun `formatTime returns unchanged (simplified)`() {
        assertEquals("095959", "095959".formatTime())
        assertEquals("6:33 pm", "6:33 pm".formatTime())
    }
}

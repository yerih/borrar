package com.mivuelto.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SerialNumberHolderTest {

    @Test
    fun `initial serial number is null`() {
        val holder = SerialNumberHolder()
        val result = holder.serialNumber.value
        assertNull(result)
    }

    @Test
    fun `setSerialNumber updates the value`() {
        val holder = SerialNumberHolder()
        holder.setSerialNumber("ABC123")
        assertEquals("ABC123", holder.serialNumber.value)
    }

    @Test
    fun `setSerialNumber can overwrite previous value`() {
        val holder = SerialNumberHolder()
        holder.setSerialNumber("FIRST")
        holder.setSerialNumber("SECOND")
        assertEquals("SECOND", holder.serialNumber.value)
    }

    @Test
    fun `serialNumber is a StateFlow`() {
        val holder = SerialNumberHolder()
        assertTrue(holder.serialNumber.replayCache.isEmpty() || holder.serialNumber.replayCache[0] == null)
    }
}

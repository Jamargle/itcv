package com.jmlb0003.itcv.utils

import com.jmlb0003.itcv.utils.DateExtensions.toShortDateString
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class DateExtensionsTest {

    @Test
    fun `on Date_toShortDateString transforms date to formatted date`() {
        val date = Date(994560000000)
        assertEquals("08 - Jul - 2001", date.toShortDateString())
    }
}

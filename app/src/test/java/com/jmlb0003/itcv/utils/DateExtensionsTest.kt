package com.jmlb0003.itcv.utils

import com.jmlb0003.itcv.utils.DateExtensions.isOlderThanYesterday
import com.jmlb0003.itcv.utils.DateExtensions.toShortDateString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date

class DateExtensionsTest {

    @Test
    fun `on Date_toShortDateString transforms date to formatted date`() {
        val date = Date(994560000000)
        assertEquals("08 - Jul - 2001", date.toShortDateString())
    }

    @Test
    fun `on Date_isOlderThanToday returns true if given date is a date before yesterday`() {
        val givenDate = Calendar.getInstance().apply {
            add(Calendar.HOUR, -25)
        }
        assertTrue(givenDate.timeInMillis.isOlderThanYesterday())
    }

    @Test
    fun `on Date_isOlderThanToday returns false if given date is a date within last 24 hours`() {
        val givenDate = Calendar.getInstance().apply {
            add(Calendar.HOUR, -23)
        }
        assertFalse(givenDate.timeInMillis.isOlderThanYesterday())
    }

    @Test
    fun `on Date_isOlderThanToday returns false if given date is a date in the future`() {
        val givenDate = Calendar.getInstance().apply {
            add(Calendar.HOUR, 1)
        }
        assertFalse(givenDate.timeInMillis.isOlderThanYesterday())
    }
}

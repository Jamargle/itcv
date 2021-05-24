package com.jmlb0003.itcv.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun `on String_normalizeTopicName given empty string returns empty`() {
        assertEquals("", "".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given blank string returns empty`() {
        assertEquals("", " ".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given a converts it to A`() {
        assertEquals("A", "a".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given A converts it to A`() {
        assertEquals("A", "A".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given ab with spaces converts it to Ab`() {
        assertEquals("Ab", " ab ".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given aaa converts it to Aaa`() {
        assertEquals("Aaa", "aaa".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given a b with spaces converts it to A B`() {
        assertEquals("A B", " a b ".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given ab-cd converts it to Ab Cd`() {
        assertEquals("Ab Cd", "ab-cd".normalizeTopicName())
    }

    @Test
    fun `on String_normalizeTopicName given ab_cd converts it to Ab Cd`() {
        assertEquals("Ab Cd", "ab_cd".normalizeTopicName())
    }
}

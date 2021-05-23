package com.jmlb0003.itcv.utils

import java.util.Locale

fun String.normalizeTopicName(): String =
    trim()
        .replace('-', ' ')
        .replace('_', ' ')
        .run {
            split(' ').joinToString(" ") { it.capitalize() }
        }

private fun String.capitalize() =
    replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(Locale.getDefault())
        } else {
            firstChar.toString()
        }
    }

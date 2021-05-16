package com.jmlb0003.itcv.di

import android.content.Context
import java.io.InputStream

class ResponseFileReader(
    private val appContext: Context
) {

    fun read(filePath: String): String {
        val stream = appContext.resources.assets.open(filePath)
        return stream.reader().use {
            it.readText()
        }
    }
}

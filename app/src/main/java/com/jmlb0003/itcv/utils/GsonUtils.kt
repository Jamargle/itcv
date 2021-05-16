package com.jmlb0003.itcv.utils

import com.google.gson.GsonBuilder
import com.jmlb0003.itcv.utils.DateExtensions.BACKEND_DATE_FORMAT
import retrofit2.converter.gson.GsonConverterFactory

object GsonUtils {

    val gson by lazy {
        GsonBuilder()
            .setDateFormat(BACKEND_DATE_FORMAT)
            .create()
    }

    val gsonConverter = GsonConverterFactory.create(gson)
}

package com.jmlb0003.itcv.di

import okhttp3.Interceptor

interface InterceptorsProvider {
    fun getInterceptors(): List<Interceptor>
}

package com.jmlb0003.itcv.data.network

import com.google.gson.annotations.SerializedName

data class ResponseFailure(
    @SerializedName("code") val errorCode: String,
    @SerializedName("message") val errorMessage: String
)

package com.jmlb0003.itcv.data.network.topic.response

import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName("names") val topics: List<String>
)

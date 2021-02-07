package com.jmlb0003.itcv.data.network.user.response.search

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val results: List<ResultItem>
)

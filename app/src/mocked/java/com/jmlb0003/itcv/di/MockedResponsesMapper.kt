package com.jmlb0003.itcv.di

object MockedResponsesMapper {
    fun getResponseFilePathFor(endpoint: String) =
        when {
            endpoint.startsWith("/users/") && endpoint.endsWith("/repos") -> "repos/jamargle_ok.json"
            endpoint.startsWith("/users/") -> "users/jamargle_ok.json"
            endpoint.startsWith("/search/users") -> "search/jamar_ok.json"
            else -> null
        }
}

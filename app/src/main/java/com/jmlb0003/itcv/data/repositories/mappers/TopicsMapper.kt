package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import com.jmlb0003.itcv.domain.model.Topic

object TopicsMapper {

    fun mapToDomain(topicsResponse: TopicsResponse) =
        with(topicsResponse) {
            topics.map {
                Topic(
                    name = it
                )
            }
        }
}

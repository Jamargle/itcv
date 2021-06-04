package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import com.jmlb0003.itcv.domain.model.Topic
import java.util.Date
import com.jmlb0003.itcv.data.model.Topic as DataTopic

object TopicsMapper {

    fun mapToDomain(topicsResponse: TopicsResponse) =
        with(topicsResponse) {
            topics.map {
                Topic(
                    name = it
                )
            }
        }

    fun mapToDomain(dataTopic: DataTopic) =
        with(dataTopic) {
            Topic(
                name = name
            )
        }

    fun mapToData(domainTopic: Topic, relatedRepoId: String, lastCacheUpdate: Date) =
        with(domainTopic) {
            DataTopic(
                name = name,
                relatedRepo = relatedRepoId,
                lastCacheUpdate = lastCacheUpdate.time
            )
        }
}

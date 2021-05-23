package com.jmlb0003.itcv.features.profile.adapter

import com.jmlb0003.itcv.domain.model.Topic

class TopicMappers(
    private val topicNormalizer: TopicNormalizer
) {
    fun mapToPresentationItems(topics: List<Topic>) =
        topicNormalizer.getTopicNamesSortedByCount(topics).map {
            mapToPresentationItem(Topic(it.first))
        }

    fun mapToPresentationItem(topic: Topic) =
        with(topic) {
            TopicListItem(
                name = name
            )
        }
}

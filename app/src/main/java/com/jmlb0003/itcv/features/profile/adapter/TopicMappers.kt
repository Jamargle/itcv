package com.jmlb0003.itcv.features.profile.adapter

import com.jmlb0003.itcv.domain.model.Topic

object TopicMappers {
    fun mapToPresentationItems(topics: List<Topic>) =
        with(topics) {
            map { mapToPresentationItem(it) }
        }

    fun mapToPresentationItem(topic: Topic) =
        with(topic) {
            TopicListItem(
                name = name
            )
        }
}

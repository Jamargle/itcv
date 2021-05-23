package com.jmlb0003.itcv.features.profile.adapter

import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.utils.normalizeTopicName

object TopicNormalizer {

    fun getTopicNamesSortedByCount(topics: List<Topic>) =
        topics.groupingBy { it.name.normalizeTopicName() }
            .eachCount().let { counts ->
                counts.toList().sortedByDescending { it.second }
            }
}

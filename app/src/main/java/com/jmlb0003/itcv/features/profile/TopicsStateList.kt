package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.features.profile.adapter.TopicListItem

sealed class TopicsStateList {
    object Loading : TopicsStateList()
    class Ready(val topics: List<TopicListItem>) : TopicsStateList()
    object Hidden : TopicsStateList()
}

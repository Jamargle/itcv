package com.jmlb0003.itcv.core.livedata

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Type for [LiveData] that will be available only once. It is handy to trigger events.
 */
class Consumable<out T>(private val content: T) {

    var hasBeenConsumed = AtomicBoolean(false)
        private set // Allow external read but not write

    fun getContentIfNotConsumed(): T? =
        if (hasBeenConsumed.compareAndSet(false, true)) {
            content
        } else {
            null
        }

    fun getContent(): T = content
}

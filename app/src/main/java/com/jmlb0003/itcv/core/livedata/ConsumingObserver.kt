package com.jmlb0003.itcv.core.livedata

import androidx.lifecycle.Observer

/**
 * Observer for data wrapper with [Consumable]
 */
class ConsumingObserver<T>(private val action: (T) -> Unit) : Observer<Consumable<T>> {
    override fun onChanged(consumable: Consumable<T>?) {
        consumable?.getContentIfNotConsumed()?.let {
            action(it)
        }
    }
}

package com.jmlb0003.itcv.data

import com.jmlb0003.itcv.core.NetworkHandler
import io.mockk.every

fun NetworkHandler.mockNetworkConnected() {
    every { isConnected } returns true
}

fun NetworkHandler.mockNetworkDisconnected() {
    every { isConnected } returns false
}

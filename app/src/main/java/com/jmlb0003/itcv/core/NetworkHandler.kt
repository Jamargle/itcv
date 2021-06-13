package com.jmlb0003.itcv.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import javax.inject.Inject

class NetworkHandler
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
@Inject constructor(
    private val context: Context
) {

    private val connectivityManager get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    val isConnected: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkConnection()
        } else {
            checkConnectionOld()
        }

    private fun checkConnection(): Boolean {
        connectivityManager?.activeNetwork?.run {
            connectivityManager?.getNetworkCapabilities(this)?.let {
                return when {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return false
    }


    @Suppress("DEPRECATION")
    private fun checkConnectionOld() =
        connectivityManager?.activeNetworkInfo?.run {
            when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            } && isConnected
        } ?: false
}

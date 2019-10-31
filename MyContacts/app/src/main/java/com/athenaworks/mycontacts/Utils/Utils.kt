package com.athenaworks.mycontacts.Utils;

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Utils{

    //Check if the device has internet conection
    companion object {
        fun isConnected(context: Context): Boolean {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)

            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }



    }

}
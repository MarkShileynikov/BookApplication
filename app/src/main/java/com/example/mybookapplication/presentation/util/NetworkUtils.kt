package com.example.mybookapplication.presentation.util

import android.content.Context
import android.net.ConnectivityManager

fun isConnectedToNetwork(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetwork

    return activeNetworkInfo != null
}
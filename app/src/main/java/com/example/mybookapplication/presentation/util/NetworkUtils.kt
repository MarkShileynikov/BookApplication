package com.example.mybookapplication.presentation.util

import android.content.Context
import android.net.ConnectivityManager


fun isConnectedToNetwork(context: Context): Boolean {
    // получаем ссылку на системный сервис
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager?.activeNetwork

    // определяем, доступно ли подключение к Интернету или нет
    return activeNetworkInfo != null
}
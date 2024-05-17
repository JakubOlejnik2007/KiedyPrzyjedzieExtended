package com.example.kiedyprzyjedzieextended.helpers

import android.util.Log
import com.github.kittinunf.fuel.httpGet

fun getJsonFromUrl(url: String): String {
    val (_, _, result) = url.httpGet().responseString()

    Log.d("fetch JSON getJsonFromUrl", result.component1() ?: result.component2()?.message ?: "")
    return result.component1() ?: result.component2()?.message ?: ""
}
package com.post_sdk.utils.ktx

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi

import com.post_sdk.utils.PostSdkConstants







// function for getting recent search list

val emptyList = ArrayList<String>()




/*
*
* method to return shared preferences instance
* */
val Context.getSharedPreference: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.M)
    get() =getSharedPreferences(PostSdkConstants.PrefConstants.PREFERENCE_NAME.value,MODE_PRIVATE)



inline fun <reified T> SharedPreferences.get(
    key: String,
    defaultValue: T
): T {
    when (T::class) {
        Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
        String::class -> return this.getString(key, defaultValue as String) as T
    }
    return defaultValue
}

inline fun <reified T> SharedPreferences.put(
    key: String, value: T
) {
    val editor = this.edit()

    when (T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        String::class -> editor.putString(key, value as String)
    }
    editor.apply()
}

fun SharedPreferences.remove(
    key: String
) {
    this.edit().remove(key).apply()
}
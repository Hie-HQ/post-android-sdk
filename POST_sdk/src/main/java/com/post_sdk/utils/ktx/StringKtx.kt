package com.post_sdk.utils.ktx

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


// function  for changing the format of date
@SuppressLint("SimpleDateFormat")
fun String.getDateInDesiredFormatWithoutUTC(input: String, output: String): String {

    val df = SimpleDateFormat(input, Locale.ENGLISH)

    val date = df.parse(this)
    val print = SimpleDateFormat(output, Locale.ENGLISH)

    return print.format(date)
}

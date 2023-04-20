package com.post_sdk.ui.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialDatePicker
import com.post_sdk.R

import com.post_sdk.ui.theme.gainsboro
import java.text.SimpleDateFormat
import java.util.*


private fun Calendar.toFormattedString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(time)
}

@Composable
fun ShowDatePicker(initialDate: Calendar, onDateSelected: (Calendar) -> Unit) {
    val context= LocalContext.current
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date:")
        .setSelection(initialDate.timeInMillis)
        .build()

    datePicker.addOnPositiveButtonClickListener {
        val newDate = Calendar.getInstance().apply {
            timeInMillis = it
        }
        onDateSelected(newDate)
    }

    datePicker.show((context as? AppCompatActivity)?.supportFragmentManager!!, null)
}


package com.post_sdk.utils

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.post_sdk.ui.theme.*

interface  OtpOperations
{
    fun otpReceived(otp:String)
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 4,
    onOtpTextChange: (String, Boolean) -> Unit,
    otpOps:OtpOperations

    ) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            if (it.length <= otpCount) {
                onOtpTextChange.invoke(it, it.length == otpCount)
                if (it.length==otpCount)
                    otpOps.otpReceived(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    Box(modifier =Modifier.weight(1f) ,
                        contentAlignment = Alignment.Center){
                        CharView(
                            index = index,
                            text = otpText
                        )
                    }
                }
            }
        }
    )
}


@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Box(modifier = Modifier
        .height(67.dp)
        .width(43.dp)

        .background(
            color = white_smoke, RoundedCornerShape(30.dp),
        )){
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Center)
            ,
            text = char,
            style = MaterialTheme.typography.h4,
            color = nero,
            textAlign = TextAlign.Center
        )
    }
}



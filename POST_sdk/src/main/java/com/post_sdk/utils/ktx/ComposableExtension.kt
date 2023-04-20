package com.post_sdk.utils.ktx

import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.post_sdk.network.ResponseWrapper


fun <T : Any> MutableState<ResponseWrapper<T>?>.loadingState(isLoaded: (pagingState: Boolean) -> Unit) {
    when (this.value) {
        is ResponseWrapper.Success<T> -> {
            isLoaded(false)
        }
        is ResponseWrapper.Loading -> {
            isLoaded(true)
        }
        is ResponseWrapper.Error -> {
            isLoaded(false)
        }
        else -> {

        }
    }
}

fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
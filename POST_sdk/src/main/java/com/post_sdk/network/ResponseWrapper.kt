package com.post_sdk.network

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val value: T) : ResponseWrapper<T>()
    data class Error(val exception:String) : ResponseWrapper<Nothing>()
    object Loading : ResponseWrapper<Nothing>()
}

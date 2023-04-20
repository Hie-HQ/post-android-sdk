package com.post_sdk.initialization

import java.io.Serializable

interface MyDataCallback: Serializable {
    fun onDataReceived(data: String)
}
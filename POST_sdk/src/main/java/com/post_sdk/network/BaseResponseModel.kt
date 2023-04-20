package com.post_sdk.network

import com.google.gson.annotations.SerializedName

open class BaseResponseModel(

) {

    @SerializedName("error")
    val error: String? = null

    @SerializedName("error_code")
    val errorCode: Int? = null

}


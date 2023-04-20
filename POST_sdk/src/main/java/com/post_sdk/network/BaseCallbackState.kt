package com.post_sdk.network

import android.os.Bundle

data class BaseCallbackState(
    val isLoading:Boolean?=null,
    val success:Boolean?=null,
    val responseCode:Int?=null,
    val message: String? =null,
    var response:Any?= null,
    var networkError:Boolean?=null,
    var clickManager:Int?=null,
    var recyclerClickPos:Int?=null,
    var tag:String?=null,
    var recycler_name:String?=null,
    var bundle:Bundle?=null
)
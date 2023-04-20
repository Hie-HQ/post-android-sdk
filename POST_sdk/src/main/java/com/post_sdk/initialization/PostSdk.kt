package com.post_sdk.initialization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.post_sdk.ui.pages.login.OnBoardingPage


import com.post_sdk.utils.PostSdkConstants
import com.post_sdk.utils.ktx.startActivityWithTransition


object PostSdk  {

    fun initialize(context: AppCompatActivity,clientId:String,clientSecret:String,callback: MyDataCallback)
    {
        // Initialize the callback instance

        val bundle=Bundle()
        bundle.putString(PostSdkConstants.InitializeConstants.clientSecret.value,clientSecret)
        bundle.putString(PostSdkConstants.InitializeConstants.clientId.value,clientId)
        bundle.putSerializable(PostSdkConstants.InitializeConstants.callback.value,callback)

        context.startActivityWithTransition(OnBoardingPage::class.java,bundle,null)
    }






}
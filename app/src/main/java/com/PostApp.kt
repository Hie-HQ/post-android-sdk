package com

import android.app.Application
import android.os.Bundle
import android.support.multidex.MultiDex
import androidx.appcompat.app.AppCompatActivity
import com.post_sdk.ui.pages.login.OnBoardingPage

import com.post_sdk.utils.PostSdkConstants
import com.post_sdk.utils.ktx.startActivityWithTransition
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*


@HiltAndroidApp
class PostApp : Application() {
    private lateinit var mInstance: PostApp
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        MultiDex.install(mInstance)



    }




}
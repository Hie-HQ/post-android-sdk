package com.post_sdk.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.post_sdk.utils.PostSdkConstants
import com.post_sdk.utils.ktx.get
import com.post_sdk.utils.ktx.getSharedPreference
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val request: Request = chain.request()



        context.getSharedPreference.let {

            if (it.get(PostSdkConstants.PrefConstants.ACCESS_TOKEN.value, "")
                    .isNotEmpty() && request.header("No-Authentication") == null
            ) {
                builder.addHeader(
                    PostSdkConstants.PrefConstants.KEY_AUTHORIZATION.value.toString(),
                    "Bearer ".plus(it.get(PostSdkConstants.PrefConstants.ACCESS_TOKEN.value, ""))
                )
            }
        }

        return chain.proceed(builder.build())
    }

}

package com.post_sdk.network

import android.content.Context
import com.post_sdk.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitBuilder {

    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient().newBuilder().followRedirects(false).followSslRedirects(false).apply {

            /*
            authenticator(TokenAuthenticator(SuperBetterApp.getInstance()))
*/
            addInterceptor(HeaderInterceptor(context))
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            connectTimeout(7, TimeUnit.MINUTES)
            readTimeout(7, TimeUnit.MINUTES)
        }.build()

    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiInterface =
        Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            addConverterFactory(gsonConverterFactory)
            client(okHttpClient)
        }.build().create(ApiInterface::class.java)
}
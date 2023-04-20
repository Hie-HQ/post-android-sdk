package com.post_sdk.network

import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.utils.PostSdkConstants
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {
    // authenticate user
    @POST(PostSdkConstants.EndPoints.authenticate)
    suspend fun authenticateUser(@Body map:PostAuthenticateRequest): Response<PostAuthenticateResponse>

    // generate otp
    @POST(PostSdkConstants.EndPoints.generateOtp)
    suspend fun generateOtp(@Body map:GenerateOtpRequest): Response<CommonSuccessResponse>

    // verify otp
    @POST(PostSdkConstants.EndPoints.authenticateOtp)
    suspend fun authenticateOtp(@Body map:AuthenticateOtpRequest): Response<OtpVerificationResponse>

    // update profile
    @PUT(PostSdkConstants.EndPoints.updateProfile)
    suspend fun updateProfile(@Path("id") id:String,@Body map:HashMap<String,Any>): Response<CommonSuccessResponse>

}
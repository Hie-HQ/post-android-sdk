package com.post_sdk.repository

import android.util.Log
import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.ApiInterface
import com.post_sdk.network.BaseRepository
import com.post_sdk.network.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class OtpRespository
@Inject constructor(private val apiInterface: ApiInterface): BaseRepository(){


    // suspended function for authenticating user
    suspend fun authenticateOtp(body: AuthenticateOtpRequest): Flow<ResponseWrapper<Any?> > = flow {
        emit(ResponseWrapper.Loading)
        try {
            val authenticateResult = apiInterface.authenticateOtp(body)
            emit(ResponseWrapper.Success(authenticateResult))

        } catch (e: Exception) {
            emit(ResponseWrapper.Error(e.message!!))
        }
    }

}

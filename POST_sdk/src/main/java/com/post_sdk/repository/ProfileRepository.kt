package com.post_sdk.repository

import android.util.Log
import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.network.ApiInterface
import com.post_sdk.network.BaseRepository
import com.post_sdk.network.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class ProfileRepository
@Inject constructor(private val apiInterface: ApiInterface): BaseRepository(){

    // suspended function for authenticating user
    suspend fun updateProfile(id:String,map:HashMap<String,Any>): Flow<ResponseWrapper<Any?> > = flow {
        emit(ResponseWrapper.Loading)
        try {
            val authenticateResult = apiInterface.updateProfile(id,map)
            Log.e("SENT","1")
            emit(ResponseWrapper.Success(authenticateResult))

        } catch (e: Exception) {
            emit(ResponseWrapper.Error(e.message!!))
        }
    }
}
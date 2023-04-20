package com.post_sdk.repository

import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.ApiInterface
import com.post_sdk.network.BaseRepository
import com.post_sdk.network.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class LoginRepository
@Inject constructor(private val apiInterface: ApiInterface):BaseRepository(){

    // suspended function for authenticating user
    suspend fun authenticateUser(body: PostAuthenticateRequest): Flow<ResponseWrapper<Response<PostAuthenticateResponse>>> = flow {
        emit(ResponseWrapper.Loading)
        try {
            val authenticateResult = apiInterface.authenticateUser(body)
            when (authenticateResult.code())
            {
                200->{
                    emit(ResponseWrapper.Success(authenticateResult))
                }
                else->{
                    var message = ""
                    message = try {
                        val jObjError = JSONObject(
                            authenticateResult.errorBody()!!.string()
                        )
                        jObjError.getString("error")
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    emit(ResponseWrapper.Error(message))
                }
            }


        } catch (e: Exception) {
            emit(ResponseWrapper.Error(e.message!!))
        }
    }


    // suspended function for authenticating user
    suspend fun generateOtp(body: GenerateOtpRequest): Flow<ResponseWrapper<Any?> > = flow {
        emit(ResponseWrapper.Loading)
        try {
            val authenticateResult = apiInterface.generateOtp(body)
            emit(ResponseWrapper.Success(authenticateResult))

        } catch (e: Exception) {
            emit(ResponseWrapper.Error(e.message!!))
        }
    }
}
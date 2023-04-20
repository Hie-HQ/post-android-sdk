package com.post_sdk.network

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException

open class BaseRepository {
    suspend fun <T> baseApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResponseWrapper<Any?> {
        return withContext(dispatcher) {
            try {
                ResponseWrapper.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                throwable.printStackTrace()
                ResponseWrapper.Error(
                    throwable.message!!
                )
               /* when (throwable) {
                    is ConnectException -> {
                        ResponseWrapper.Error(
                            throwable
                        )
                    }
                    is HttpException -> {
                        val errorResponse = convertErrorBody(throwable)
                        if (errorResponse != null) {
                            ResponseWrapper.Error(errorResponse.error, errorResponse.errorCode)
                        } else {
                            ResponseWrapper.Error(
                                PostSdk.getInstance()
                                    .getString(R.string.error_something_went_wrong), errorResponse
                            )
                        }
                    }
                    else -> {
                        ResponseWrapper.Error(
                            PostSdk.getInstance()
                                .getString(R.string.error_something_went_wrong)
                        )
                    }
                }*/
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): BaseResponseModel? {
        return try {

            throwable.response()?.errorBody()?.string().let {
                Gson().fromJson(it, BaseResponseModel::class.java)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}

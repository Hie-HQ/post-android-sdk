package com.post_sdk.ui.pages.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.BaseCallbackState
import com.post_sdk.network.ResponseWrapper
import com.post_sdk.repository.LoginRepository
import com.post_sdk.utils.ktx.loadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

    //  authenticate user
    val authenticateUser: MutableState<ResponseWrapper<Response<PostAuthenticateResponse>>?> = mutableStateOf(null)
    fun authenticate2(body: PostAuthenticateRequest) {
        viewModelScope.launch {
            repository.authenticateUser(body).onEach {
                authenticateUser.value = it
            }.launchIn(viewModelScope)
        }
    }

    //  generate otp
    val generateOtpState: MutableState<BaseCallbackState?> = mutableStateOf(null)
    fun generateOtp(body: GenerateOtpRequest) {
        viewModelScope.launch {
            repository.generateOtp(body).onEach {response->
                when (response) {

                    is ResponseWrapper.Success<*> -> {
                        (response.value as Response<CommonSuccessResponse>).apply {
                            when (code()) {
                                200 -> {
                                    body().apply {
                                        generateOtpState.value=
                                            BaseCallbackState(
                                                    isLoading = false,
                                                    success = true,
                                                    response = this
                                                )


                                    }
                                }

                                else -> {
                                    var message = ""
                                    message = try {
                                        val jObjError = JSONObject(
                                            response.value.errorBody()!!.string()
                                        )
                                        jObjError.getString("error")
                                    } catch (e: Exception) {
                                        e.message.toString()
                                    }
                                    generateOtpState.value=
                                            BaseCallbackState(
                                                isLoading = false,
                                                success = false,
                                                message = message
                                    )
                                }
                            }
                        }
                    }
                    is ResponseWrapper.Loading->
                    {
                        generateOtpState.value= BaseCallbackState(isLoading = true,)
                    }

                    is ResponseWrapper.Error -> {
                        generateOtpState.value=(
                                BaseCallbackState(
                                    isLoading = false,
                                    success = false,
                                    message = "Something went wrong!"
                                )
                        )
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

}
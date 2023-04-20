package com.post_sdk.ui.pages.otp_verification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.BaseCallbackState
import com.post_sdk.network.ResponseWrapper
import com.post_sdk.repository.LoginRepository
import com.post_sdk.repository.OtpRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class OtpViewModel  @Inject constructor(
    private val repository: OtpRespository
): ViewModel() {

    //  otp authenticate
    val authenticateOtpState: MutableState<BaseCallbackState?> = mutableStateOf(null)
    fun authenticateOtp(body: AuthenticateOtpRequest) {
        viewModelScope.launch {
            repository.authenticateOtp(body).onEach {response->
                when (response) {
                    is ResponseWrapper.Success<*> -> {
                        (response.value as Response<OtpVerificationResponse>).apply {
                            when (code()) {
                                200 -> {
                                    body().apply {
                                        authenticateOtpState.value=
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
                                    authenticateOtpState.value=
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
                        authenticateOtpState.value= BaseCallbackState(isLoading = true,)
                    }

                    is ResponseWrapper.Error -> {
                        authenticateOtpState.value=(
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
package com.post_sdk.ui.pages.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.network.BaseCallbackState
import com.post_sdk.network.ResponseWrapper
import com.post_sdk.repository.OtpRespository
import com.post_sdk.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val repository: ProfileRepository
): ViewModel() {

    //  update profile state


    val updateProfileState: MutableState<BaseCallbackState?> = mutableStateOf(null)
    fun updateProfile(id:String,map:HashMap<String,Any>) {
        viewModelScope.launch {
            repository.updateProfile(id, map).onEach {response->
                when (response) {
                    is ResponseWrapper.Success<*> -> {
                        (response.value as Response<CommonSuccessResponse>).apply {
                            when (code()) {
                                200 -> {
                                    Log.e("SENT","1")
                                    body().apply {
                                        updateProfileState.value=
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
                                    updateProfileState.value=
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
                        updateProfileState.value= BaseCallbackState(isLoading = true,)
                    }

                    is ResponseWrapper.Error -> {
                        updateProfileState.value=(
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
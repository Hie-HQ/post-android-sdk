package com.post_sdk.ui.pages.otp_verification

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.post_sdk.BuildConfig
import com.post_sdk.model.request.AuthenticateOtpRequest
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.ResponseWrapper
import com.post_sdk.ui.components.AppBarWithBack
import com.post_sdk.ui.components.CircularIndeterminateProgressBar
import com.post_sdk.ui.pages.profile.SetUpProfilePage


import com.post_sdk.ui.theme.POST_sdkTheme
import com.post_sdk.ui.theme.grey
import com.post_sdk.ui.theme.nero
import com.post_sdk.ui.theme.storm_gray
import com.post_sdk.utils.*
import com.post_sdk.utils.ktx.loadingState

import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class OtpVerificationPage : ComponentActivity() {
    var bundle=Bundle()
    lateinit var customForm: PostAuthenticateResponse.PostAuthenticateData.PostAuthenticateCustomForm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle=intent.extras!!
       // customForm=bundle.getSerializable("form")
        customForm=    if (Build.VERSION.SDK_INT >= 33)
            bundle.getSerializable("form", PostAuthenticateResponse.PostAuthenticateData.PostAuthenticateCustomForm::class.java)!!
        else
            bundle.getSerializable("form") as PostAuthenticateResponse.PostAuthenticateData.PostAuthenticateCustomForm

        setContent {
            POST_sdkTheme {
                val backgroundColor = remember { mutableStateOf(Color.White) }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                     color = backgroundColor.value
                ) {
                    OtpVerification(bundle,customForm,backgroundColor)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OtpVerification(
    bundle: Bundle,
    customForm: PostAuthenticateResponse.PostAuthenticateData.PostAuthenticateCustomForm,
    backgroundColor: MutableState<Color>
) {
    LocalContext.current.translucentActivity()
    val number= bundle.getString("number")
    val viewModel: OtpViewModel = hiltViewModel()
    val progressBarAlpha = remember { mutableStateOf(0.5f) }
    val progressBar = remember { mutableStateOf(false) }
    var otpValue by remember {
        mutableStateOf("")
    }
    val context= LocalContext.current


   Box(modifier = Modifier.fillMaxSize(),){
       if (customForm.backgroundImage!=null)
       {
           GlideImage(
               model = BuildConfig.S3_BASE_URL+customForm.backgroundImage,
               contentDescription ="",
               modifier = Modifier.fillMaxSize(),
               contentScale = ContentScale.FillBounds
           )
       }else
       {
           backgroundColor.value=remember { Color(android.graphics.Color.parseColor(customForm.backgroundColor)) }
           val systemUiController = rememberSystemUiController()
           systemUiController.setSystemBarsColor(
               color = backgroundColor.value
           )
       }
       Column(
           modifier = Modifier.fillMaxSize(),
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           CircularIndeterminateProgressBar(isDisplayed = progressBar.value, 0.4f, alpha = progressBarAlpha.value)

           customForm.apply {
               AppBarWithBack(brandLogo = brandLogo, brandName = brandName)
           }



           Spacer(modifier = Modifier.height(40.dp))
           Text(text = "We have sent a verification code to \n" +
                   "+91 $number",
               color = grey,
               textAlign = TextAlign.Center,
               modifier = Modifier.padding(bottom = 45.dp),
               fontSize = 14.sp
           )

           OtpTextField(
               otpText = otpValue,
               otpCount = 6,
               onOtpTextChange = { value, _ ->
                   otpValue = value
               },
               otpOps = object : OtpOperations {
                   override fun otpReceived(otp: String) {
                       viewModel.authenticateOtp(AuthenticateOtpRequest(
                           countryCode = 91,
                           mobile = number!!.toLong(),
                           otp = otp.toLong()
                       ))
                       // progressBarAlpha.value=0.5f
                       //
                   }
               },
               modifier = Modifier.padding(start = 40.dp, end = 40.dp)
           )


           Box(modifier = Modifier
               .wrapContentWidth()
               .padding(top = 40.dp)
               .border(1.dp, nero, shape = RoundedCornerShape(20.dp))){
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
               ) {
                   Text(text = "Resend SMS",
                       color = storm_gray,
                       fontSize = 14.sp
                   )
               }
           }
       }
   }

    viewModel.authenticateOtpState.value?.let { it ->
        it.apply {
            when(success)
            {
                true->
                {
                    (response as OtpVerificationResponse).apply {
                        val bundleOtp= Bundle()
                        bundleOtp.putParcelable(PostSdkConstants.BundleEnums.OTP_VERIFICATION_CUSTOM_FORM.value, data)
                        context.StartActivityWithTransition(cls = SetUpProfilePage::class.java, bundle =bundleOtp , flag = null)
                        (context as Activity?)?.finish()
                    }
                }
                else->{}
            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    POST_sdkTheme {
       // OtpVerification(bundle)
    }
}
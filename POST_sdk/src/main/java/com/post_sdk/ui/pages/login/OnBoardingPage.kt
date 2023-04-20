package com.post_sdk.ui.pages.login

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.post_sdk.BuildConfig
import com.post_sdk.R
import com.post_sdk.initialization.MyDataCallback
import com.post_sdk.model.request.GenerateOtpRequest
import com.post_sdk.model.request.PostAuthenticateRequest
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.PostAuthenticateResponse
import com.post_sdk.network.ResponseWrapper
import com.post_sdk.ui.components.CircleImage
import com.post_sdk.ui.components.CircularIndeterminateProgressBar

import com.post_sdk.ui.pages.otp_verification.OtpVerificationPage
import com.post_sdk.ui.theme.POST_sdkTheme
import com.post_sdk.ui.theme.white
import com.post_sdk.utils.PostSdkConstants
import com.post_sdk.utils.StartActivityWithTransition
import com.post_sdk.utils.ktx.getSharedPreference
import com.post_sdk.utils.ktx.loadingState
import com.post_sdk.utils.ktx.put
import com.post_sdk.utils.translucentActivity

import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response


@AndroidEntryPoint
class OnBoardingPage  : ComponentActivity() {



    var bundle=Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle= intent.extras!!

        setContent {
            POST_sdkTheme {
                // A surface container using the 'background' color from the theme
                SurfaceLogin(bundle)
            }
        }
    }
}



@Composable
fun SurfaceLogin(bundle: Bundle) {
    // Remember the current background color using a mutable state variable
    val backgroundColor = remember { mutableStateOf(Color.White) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor.value
    ){
        // Add your content here...
       LocalContext.current.translucentActivity()
        LoginPage(bundle,backgroundColor)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoginPage(bundle: Bundle, backgroundColor: MutableState<Color>) {
    val progressBar = remember { mutableStateOf(false) }
    val progressBarAlpha = remember { mutableStateOf(0f) }
    val viewModel: LoginViewModel = hiltViewModel()
    // hit api inside this coroutine scope
    LaunchedEffect(true) {
        viewModel.authenticate2(
            PostAuthenticateRequest(
                bundle.getString(PostSdkConstants.InitializeConstants.clientId.value,""),
                bundle.getString(PostSdkConstants.InitializeConstants.clientSecret.value,""),
            )
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {


        // show progress indicator here
        CircularIndeterminateProgressBar(isDisplayed = progressBar.value, 0.4f, alpha = progressBarAlpha.value)

        // observe the data of authenticate user
        viewModel.authenticateUser.value?.let { it ->
            when (it) {
                is ResponseWrapper.Success<Response<PostAuthenticateResponse>> -> {
                    val response= it.value.body() as PostAuthenticateResponse
                    LocalContext.current.getSharedPreference.apply {
                        put(PostSdkConstants.PrefConstants.ACCESS_TOKEN.value,response.data.tokens.access.token)
                        put(PostSdkConstants.PrefConstants.REFRESH_TOKEN.value,response.data.tokens.refresh.token)
                    }

                    if (response.data.customForm.backgroundImage!=null)
                    {
                        GlideImage(
                            model = BuildConfig.S3_BASE_URL+response.data.customForm.backgroundImage,
                            contentDescription ="",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }else
                    {
                        backgroundColor.value=remember { Color(android.graphics.Color.parseColor(response.data.customForm.backgroundColor)) }
                        val systemUiController = rememberSystemUiController()
                        systemUiController.setSystemBarsColor(
                            color = backgroundColor.value
                        )
                    }

                    TopAppBar(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 45.dp),
                        title = {
                            response.data.customForm.apply {
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (brandLogo!=null)
                                    {
                                        CircleImage(url = brandLogo!!, size = 70)
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    else if (brandName!=null)
                                    {
                                        Text(text = brandName!!,
                                            color=Color.Black,
                                            modifier = Modifier.wrapContentWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                            }
                        },
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    )
                    LoginForm(
                        Modifier
                            .align(Alignment.Center)
                            .padding(end = 16.dp, start = 16.dp),
                        response.data.customForm,
                        viewModel,
                        progressBarAlpha,
                        bundle,
                        progressBar
                    )
                }
                else -> {}
            }
        }


        // observe loading state here
        viewModel.authenticateUser.loadingState {
            progressBar.value = it
        }

    }
}

@Composable
fun LoginForm(
    modifier: Modifier,
    customForm: PostAuthenticateResponse.PostAuthenticateData.PostAuthenticateCustomForm,
    viewModel: LoginViewModel,
    progressBarAlpha: MutableState<Float>,
    bundle: Bundle,
    progressBar: MutableState<Boolean>,

    )
{
    val context= LocalContext.current
    var phoneNumberState by remember {
        mutableStateOf(TextFieldValue())
    }
    val callback:MyDataCallback=  when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> bundle.getSerializable(PostSdkConstants.InitializeConstants.callback.value,MyDataCallback::class.java)!!
        else -> @Suppress("DEPRECATION") bundle.getSerializable(PostSdkConstants.InitializeConstants.callback.value)!! as MyDataCallback
    }

    var buttonColor= "#1E1E1E"
    if (customForm.buttonColor!=null)
        buttonColor=customForm.buttonColor!!
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp, color = colorResource(id = R.color.gainsboro),
                    shape = RoundedCornerShape(
                        size = 100.dp
                    )
                ),
            shape = RoundedCornerShape(
                size = 100.dp
            ),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = colorResource(id = R.color.gainsboro),
                focusedBorderColor = colorResource(id = R.color.gainsboro),
                backgroundColor = white
                ),

            placeholder = { Text(text = "Phone number",
                fontSize = 16.sp,
                color = colorResource(id = R.color.mortar)
            )},
            value = phoneNumberState, onValueChange ={
                phoneNumberState=it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ).copy(imeAction = ImeAction.Done),

            singleLine = true,


            )
        Button(onClick = {
            when(phoneNumberState.text.isEmpty())
            {
                true->
                {
                    Toast.makeText(context,"Please provide a phone number", Toast.LENGTH_SHORT).show()
                }
                false->{
                    // show progress indicator here
                    progressBarAlpha.value=0.5f
                    viewModel.generateOtp(GenerateOtpRequest(countryCode = 91, mobile = phoneNumberState.text.toLong()))
                }
            }
        },
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = remember { Color(android.graphics.Color.parseColor(buttonColor)) }),
            shape = RoundedCornerShape(100.dp)
        ) {

            var buttonTextColor="#FFFFFF"

            if (customForm.buttonTextColor!=null)
                buttonTextColor=customForm.buttonTextColor!!
            Text(text = "Continue",
                modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                ),
                color =remember { Color(android.graphics.Color.parseColor(buttonTextColor)) }
            )
        }
        Row (modifier= Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Divider(
                modifier=Modifier.weight(1f),
                color = colorResource(id = R.color.shady_lady), thickness = 0.5.dp)
            Text(text = "OR",
                modifier=Modifier.weight(0.2f)
                , color = colorResource(id = R.color.shady_lady),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Divider(
                modifier=Modifier.weight(1f),
                color = colorResource(id = R.color.shady_lady), thickness = 0.5.dp)
        }
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 30.dp, end = 30.dp, bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(60.dp)
                )
                .padding(3.dp)
                .border(
                    width = 1.dp, color = colorResource(id = R.color.gainsboro),
                    RoundedCornerShape(60.dp)
                )
            ){
                Image(painter = painterResource(id = R.drawable.ic_uim_snapchat_ghost),
                    contentDescription = "",
                    modifier= Modifier
                        .align(Alignment.Center)
                        .wrapContentHeight()
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(width = 10.dp))
            Box(modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(60.dp)
                )
                .padding(3.dp)
                .border(
                    width = 1.dp, color = colorResource(id = R.color.gainsboro),
                    RoundedCornerShape(60.dp)
                )
            ){
                Image(painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "",
                    modifier= Modifier
                        .align(Alignment.Center)
                        .wrapContentHeight()
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(width = 10.dp))
            Box(modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(60.dp)
                )
                .padding(3.dp)
                .border(
                    width = 1.dp, color = colorResource(id = R.color.gainsboro),
                    RoundedCornerShape(60.dp)
                )
            ){
                Image(painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "",
                    modifier= Modifier
                        .align(Alignment.Center)
                        .wrapContentHeight()
                        .padding(16.dp)
                )
            }
        }



    }


    //observe generate otp state here
    viewModel.generateOtpState.value?.let { it->
        it.apply {
            progressBar.value = isLoading!!

            when(success)
            {
                true->{
                    (response as CommonSuccessResponse).apply {
                        callback.onDataReceived("OTP SEND")
                        val bundle=Bundle()
                        bundle.putString("number", phoneNumberState.text)
                        bundle.putSerializable("form", customForm)
                        LocalContext.current.StartActivityWithTransition(cls = OtpVerificationPage::class.java, bundle =bundle , flag =null )
                    }
                }
                else->{

                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    POST_sdkTheme {
        /*LoginPage(viewModel, bundle)*/
    }
}
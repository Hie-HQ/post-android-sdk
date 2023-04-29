package com.post_sdk.ui.pages.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.post_sdk.model.response.CommonSuccessResponse
import com.post_sdk.model.response.OtpVerificationResponse
import com.post_sdk.ui.components.AppBarWithBack

import com.post_sdk.ui.theme.POST_sdkTheme
import com.post_sdk.ui.theme.gainsboro
import com.post_sdk.ui.theme.white
import com.post_sdk.utils.PostSdkConstants
import com.post_sdk.utils.ktx.getParcelableOrNull
import com.post_sdk.utils.translucentActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class SetUpProfilePage : ComponentActivity() {
    var bundle= Bundle()

    lateinit var customFormData: OtpVerificationResponse.OtpVerificationData
    var position=0
    var currentPositionOfCustomForm=0
    var stepIncremented=false

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle=intent.extras!!
        customFormData= bundle.getParcelableOrNull(PostSdkConstants.BundleEnums.OTP_VERIFICATION_CUSTOM_FORM.value)!!
        setContent {
            POST_sdkTheme {
                // A surface container using the 'background' color from the theme
                val backgroundColor = remember { mutableStateOf(Color.Transparent) }

                val viewModel: ProfileViewModel = hiltViewModel()

                var fields by remember {
                    mutableStateOf(customFormData.customForm.customForm[currentPositionOfCustomForm].fields.toMutableList())
                }

                Box(modifier = Modifier.fillMaxSize()){
                    if (customFormData.customForm.backgroundImage!=null)
                    {
                        GlideImage(
                            model = PostSdkConstants.NetworkingConstants.S3_BASE_URL.value+customFormData.customForm.backgroundImage,
                            contentDescription ="",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }else
                    {
                        backgroundColor.value=remember { Color(android.graphics.Color.parseColor(customFormData.customForm.backgroundColor)) }
                        val systemUiController = rememberSystemUiController()
                        systemUiController.setSystemBarsColor(
                            color = backgroundColor.value
                        )
                    }
                    Scaffold(
                        backgroundColor=backgroundColor.value,
                        topBar = {
                            customFormData.customForm.apply {
                                AppBarWithBack(brandLogo = brandLogo, brandName = brandName)
                            }
                        }
                    ) { innerPadding ->
                        SetUpProfile(customFormData =customFormData ,innerPadding ,fields)

                        viewModel.updateProfileState.value?.let {

                            it.apply {
                                when(success)
                                {
                                    true->
                                    {
                                        (response as CommonSuccessResponse).apply {
                                            if (currentPositionOfCustomForm<customFormData.customForm.customForm.size-1)
                                            {
                                                if (stepIncremented)
                                                {
                                                    val nextPosition = currentPositionOfCustomForm + 1
                                                    val fieldsList = customFormData.customForm.customForm[nextPosition].fields.toMutableList()
                                                    fields = fieldsList
                                                    currentPositionOfCustomForm++
                                                    stepIncremented=false
                                                }

                                            }else if(currentPositionOfCustomForm==customFormData.customForm.customForm.size-1){
                                                Log.e("HERE","SEND_CALLBACK_TO_CLIENT")
                                            } else {

                                            }
                                        }
                                    }
                                    else->{}
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun SetUpProfile(
        customFormData: OtpVerificationResponse.OtpVerificationData,
        innerPadding: PaddingValues,
        fields: MutableList<OtpVerificationResponse.OtpVerificationData.OtpVerificationCustomForm.OtpVerificationCustomFormInner.CustomFormFields>,
    ) {
        LocalContext.current.translucentActivity()


        CustomFormSetUpProfile(fields,customFormData,innerPadding)





    }



    // composable function for showing custom form fields
    @Composable
    fun CustomFormSetUpProfile(
        fields: MutableList<OtpVerificationResponse.OtpVerificationData.OtpVerificationCustomForm.OtpVerificationCustomFormInner.CustomFormFields>,
        customFormData: OtpVerificationResponse.OtpVerificationData,
        innerPadding: PaddingValues,
    ) {
        var showAddressForm by remember {
            mutableStateOf(false)
        }

        val activty= (LocalContext.current as Activity)

        showAddressForm = customFormData.user.addresses.isNotEmpty()
        
        BackHandler(onBack = {
            if (!showAddressForm)
            {
                if (customFormData.user.addresses.isNotEmpty())
                    showAddressForm=true
            }
            else
                activty.finish()

        })

        var selectedAddressId=""
        var address by remember {
            mutableStateOf(customFormData.user.addresses)
        }

        val viewModel:ProfileViewModel= hiltViewModel()

        var buttonColor="#FFFFFF"
        if (customFormData.customForm.buttonColor!=null)
            buttonColor=customFormData.customForm.buttonColor!!
        var buttonTextColor="#FFFFFF"

        if (customFormData.customForm.buttonTextColor!=null)
            buttonTextColor=customFormData.customForm.buttonTextColor!!
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = innerPadding.calculateTopPadding())

        ) {

            if (!showAddressForm)
            {
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
                items(fields){item ->
                    when(item.type){
                        PostSdkConstants.CustomFormType.TEXT.value->{
                            var textValue by remember {
                                mutableStateOf(TextFieldValue())
                            }


                            if(stepIncremented)
                            {
                                textValue= TextFieldValue("")
                            }

                            OutlinedTextField(value = textValue, onValueChange ={
                                textValue=it
                                item.value=textValue.text
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp)
                                    .border(
                                        width = 1.dp,
                                        color = gainsboro,
                                        shape = RoundedCornerShape(100.dp)
                                    ),
                                shape = RoundedCornerShape(100.dp),
                                placeholder = { Text(text = item.placeholder,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.mortar)
                                )},

                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                ).copy(imeAction = ImeAction.Next),
                                singleLine = true,

                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = colorResource(id = R.color.gainsboro),
                                    focusedBorderColor = colorResource(id = R.color.gainsboro),
                                    backgroundColor = white
                                ),
                            )
                        }
                        PostSdkConstants.CustomFormType.DATE.value->{
                            DatePickerTextField(item)
                        }
                        PostSdkConstants.CustomFormType.NUMBER.value->{
                            var textNumberValue by remember {
                                mutableStateOf(TextFieldValue())
                            }
                            OutlinedTextField(value = textNumberValue, onValueChange ={
                                textNumberValue=it
                                item.value=textNumberValue.text
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp)
                                    .border(
                                        width = 1.dp,
                                        color = gainsboro,
                                        shape = RoundedCornerShape(100.dp)
                                    ),
                                shape = RoundedCornerShape(100.dp),
                                placeholder = { Text(text = item.placeholder,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.mortar)
                                )},
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ).copy(imeAction = ImeAction.Next),
                                singleLine = true,

                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = colorResource(id = R.color.gainsboro),
                                    focusedBorderColor = colorResource(id = R.color.gainsboro),
                                    backgroundColor = white
                                ),
                            )
                        }
                        PostSdkConstants.CustomFormType.SELECT.value->{
                            SpinnerDemo(item)
                        }
                    }
                }
                item {
                    BottomToListColumn(customFormData)
                }
            }
            else
            {
                item {
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(text = "Select from given address",
                        fontSize = 18.sp,
                        color = white,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
                items(address.size){index ->
                    val item=address[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable{
                            val list:ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject> =
                                address.clone() as ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject>
                            for (i in list.indices)
                            {
                                list[i].selected=false
                            }
                            list[index].selected=true
                            selectedAddressId=list[index]._id
                            address.clear()
                            address= list.clone() as ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject>
                        }
                    ) {
                        val checkedImage = if (item.selected) R.drawable.ic_checked else R.drawable.ic_uncheck

                        Image(painterResource(id = checkedImage), contentDescription ="", modifier = Modifier.wrapContentSize() )

                        Spacer(modifier = Modifier.width(15.dp))

                        item.apply {
                            Text(text = "$address1, $address2, $landmark, $city, $state, $country, $pinCode",
                                fontSize = 15.sp,
                                color = white
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(onClick = {
                                     val map= HashMap<String,Any>()
                        map["id"]=selectedAddressId
                        val publishMap= HashMap<String,Any>()
                        publishMap["step"]="address"
                        publishMap["payload"]=map

                        viewModel.updateProfile(customFormData.user.id,publishMap)

                    },
                        modifier= Modifier
                            .fillMaxWidth()
                        ,
                        colors = ButtonDefaults.buttonColors(backgroundColor = remember { Color(android.graphics.Color.parseColor(buttonColor)) }),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(text = "Set as default",
                            modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                            ),
                            color =remember { Color(android.graphics.Color.parseColor(buttonTextColor)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = {
                                     showAddressForm=false
                    },
                        modifier= Modifier
                            .fillMaxWidth()
                        ,
                        colors = ButtonDefaults.buttonColors(backgroundColor = remember { Color(android.graphics.Color.parseColor(buttonColor)) }),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(text = "Add new Address",
                            modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                            ),
                            color =remember { Color(android.graphics.Color.parseColor(buttonTextColor)) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }


        }
    }

    // composable function for showing address that are available
    @Composable
    fun AddressList(
        fields: MutableList<OtpVerificationResponse.OtpVerificationData.OtpVerificationCustomForm.OtpVerificationCustomFormInner.CustomFormFields>,
        customFormData: OtpVerificationResponse.OtpVerificationData,
        innerPadding: PaddingValues,
        addressFormAlpha: MutableState<Float>,
        customFormAlpha: MutableState<Float>
    )
    {

        var address by remember {
            mutableStateOf(customFormData.user.addresses)
        }


        val listState = rememberLazyListState()
        var buttonColor="#FFFFFF"
        if (customFormData.customForm.buttonColor!=null)
            buttonColor=customFormData.customForm.buttonColor!!
        var buttonTextColor="#FFFFFF"

        if (customFormData.customForm.buttonTextColor!=null)
            buttonTextColor=customFormData.customForm.buttonTextColor!!
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .alpha(addressFormAlpha.value),
            state = listState
        ){
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Select from given address",
                    fontSize = 18.sp,
                    color = white,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            items(address.size){index ->
                val item=address[index]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable{
                        val list:ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject> =
                            address.clone() as ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject>
                        for (i in list.indices)
                        {
                            list[i].selected=false
                        }
                        list[index].selected=true
                        address.clear()
                        address= list.clone() as ArrayList<OtpVerificationResponse.OtpVerificationData.OtpVerificationUserData.OtpAddressObject>
                    }
                ) {
                    val checkedImage = if (item.selected) R.drawable.ic_checked else R.drawable.ic_uncheck

                    Image(painterResource(id = checkedImage), contentDescription ="", modifier = Modifier.wrapContentSize() )

                    Spacer(modifier = Modifier.width(10.dp))

                    item.apply {
                        Text(text = "$address1, $address2, $landmark, $city, $state, $country, $pinCode",
                            fontSize = 15.sp,
                            color = white
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Button(onClick = {

                },
                    modifier= Modifier
                        .fillMaxWidth()
                    ,
                    colors = ButtonDefaults.buttonColors(backgroundColor = remember { Color(android.graphics.Color.parseColor(buttonColor)) }),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(text = "Set as default",
                        modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                        ),
                        color =remember { Color(android.graphics.Color.parseColor(buttonTextColor)) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {


                },
                    modifier= Modifier
                        .fillMaxWidth()
                    ,
                    colors = ButtonDefaults.buttonColors(backgroundColor = remember { Color(android.graphics.Color.parseColor(buttonColor)) }),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(text = "Add new Address",
                        modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                        ),
                        color =remember { Color(android.graphics.Color.parseColor(buttonTextColor)) }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }


    @Composable
    fun BottomToListColumn(customFormData: OtpVerificationResponse.OtpVerificationData)
    {
        val viewModel:ProfileViewModel= hiltViewModel()

        Spacer(modifier = Modifier.height(130.dp))
        Row(
            modifier = Modifier
                .wrapContentWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.ic_unchecked_checkbox),
                contentDescription ="" )
            Text(text = stringResource(R.string.i_agree_with),
                fontSize = 16.sp,
                color = colorResource(id = R.color.mortar),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(text = stringResource(R.string.terms_and_condition),
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.mortar),
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        Button(onClick = {
            val map =HashMap<String,Any>()
            val publishMap=HashMap<String,Any>()
            customFormData.customForm.customForm[position].apply {
                customFormData.customForm.customForm[position].fields.apply {
                    for (i in this.indices)
                    {
                        map[this[i].key]=this[i].value!!
                    }
                }
                publishMap["step"]=key
                publishMap["payload"]=map
            }

            stepIncremented=true
            viewModel.updateProfile(customFormData.user.id,publishMap)
            position++


            // currentPositionOfCustomForm++

        },
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.nero)),
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(text = "Continue",
                modifier=Modifier.padding(top = 8.dp, bottom = 8.dp
                ),
                color = colorResource(id = R.color.white)
            )
        }
    }

    @Composable
    fun DatePickerTextField(
        item: OtpVerificationResponse.OtpVerificationData.OtpVerificationCustomForm.OtpVerificationCustomFormInner.CustomFormFields,

        ) {


        // Fetching the Local Context
        val mContext = LocalContext.current

        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()



        val initialValue= when(item.value==null)
        {
            true->
                item.placeholder
            false->
                ""
        }


        var textValue by remember { mutableStateOf(TextFieldValue(initialValue)) }




        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                val month=  if (mMonth.toString().length==1)
                    "0${mMonth+1}"
                else
                    (mMonth+1).toString()

                val day=  if (mDayOfMonth.toString().length==1)
                    "0${mDayOfMonth}"
                else
                    mDayOfMonth.toString()
                item.value="$mYear-$month-$day"
                textValue   = TextFieldValue("$mDayOfMonth/${mMonth+1}/$mYear")

            }, mYear, mMonth, mDay
        )









        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .border(
                    width = 1.dp,
                    color = gainsboro,
                    shape = RoundedCornerShape(100.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {


            TextField(
                value = textValue,
                onValueChange = { newValue ->
                    textValue = newValue
                },
                modifier = Modifier
                    .padding(start = 15.dp)
                    .clickable {
                        mDatePickerDialog.show()
                    }
                ,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                readOnly = true,
                enabled = false
            )
            Image(painter = painterResource(id = R.drawable.ic_material_symbols_calendar_month_outline_rounded),
                modifier = Modifier.padding(end = 10.dp)
                , contentDescription ="" )
        }

    }

    @Composable
    fun SpinnerDemo(items: OtpVerificationResponse.OtpVerificationData.OtpVerificationCustomForm.OtpVerificationCustomFormInner.CustomFormFields) {
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }

        val selectedItem = items.options!![selectedIndex]

        // Dropdown menu
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .border(
                width = 1.dp,
                color = gainsboro,
                shape = RoundedCornerShape(100.dp)
            ),
        ) {
            Text(
                text = selectedItem.label,
                modifier = Modifier
                    .clickable(onClick = { expanded = true })
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                items.options!!.forEachIndexed { index, item ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        items.value=item.label
                        Text(text = item.label)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview3() {
        POST_sdkTheme {
            //  SetUpProfile(customForm)
        }
    }
}

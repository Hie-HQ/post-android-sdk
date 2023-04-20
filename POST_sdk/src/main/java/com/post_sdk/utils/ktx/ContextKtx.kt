package com.post_sdk.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.datepicker.MaterialDatePicker
import com.post_sdk.R
import com.post_sdk.utils.ktx.startActivityWithTransitionCompose
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Context.showToast(message:String)
{
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

@Composable
fun Context.translucentActivity()
{
    val activity = this as? Activity
    activity?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

@Composable
fun Context.StartActivityWithTransition(cls: Class<*>?, bundle: Bundle?, flag: Int? )
{
    val activity = LocalContext.current as? Activity
    val intent = Intent(this,cls)
    if (flag!=null)
        intent.flags = flag
    if (bundle!=null)
        intent.putExtras(bundle)
    startActivity(intent)
    activity?. overridePendingTransition(R.anim.slide_up, R.anim.fade_out)
}





package com.post_sdk.utils.ktx

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.post_sdk.R


fun AppCompatActivity.startActivityWithTransition(cls: Class<*>?, bundle: Bundle?, flag: Int? )
{
    val intent = Intent(this,cls)
    if (flag!=null)
        intent.flags = flag
    if (bundle!=null)
        intent.putExtras(bundle)
    startActivity(intent)
    overridePendingTransition(R.anim.slide_up, R.anim.fade_out)
}




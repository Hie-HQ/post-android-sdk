package com.post_lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.post_sdk.initialization.MyDataCallback
import com.post_sdk.initialization.PostSdk


class MainActivity : AppCompatActivity(), MyDataCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btOpen= findViewById<Button>(R.id.bt_open)

btOpen.setOnClickListener {
    PostSdk.initialize(this,"post_test_62b46b90-8f31-4286-8a21-9511b2abaae3"
        ,"post_test_0456fe32-e32d-4287-8c44-4299b6b9cab3",this)

}



    }

    override fun onDataReceived(data: String) {
        Log.e("DONE",data)
    }


}
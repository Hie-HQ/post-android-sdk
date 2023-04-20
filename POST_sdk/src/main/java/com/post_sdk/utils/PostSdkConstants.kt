package com.post_sdk.utils

class PostSdkConstants {

    // enum for PREF
    enum class PrefConstants(val value:String)
    {
        PREFERENCE_NAME("post_sdk_pref_name"),
        ACCESS_TOKEN("ACCESS_TOKEN"),
        REFRESH_TOKEN("REFRESH_TOKEN"),
        KEY_AUTHORIZATION("Authorization"),
    }

    // enum for initialization
    enum class InitializeConstants(val value:String)
    {
        clientId("clientId"),
        callback("callback"),
        clientSecret("clientSecret")
    }

    // enum class for bundle when sending data using intent
    enum class BundleEnums(val value:String){
         OTP_VERIFICATION_CUSTOM_FORM("OTP_VERIFICATION_CUSTOM_FORM")
    }

    // enum class for custom form fields
    enum class CustomFormType(val value:String)
    {
        TEXT("text"),
        SELECT("select"),
        DATE("date"),
        NUMBER("number"),
    }

    // singleton class for all the end points that are used in networking
    object EndPoints
    {
     const val   authenticate=("sdk-user/auth/sdk")
     const val   generateOtp=("sdk-user/auth/generate-otp")
     const val   authenticateOtp=("sdk-user/auth/authenticate")
     const val   updateProfile=("sdk-user/profile/{id}")
    }
}
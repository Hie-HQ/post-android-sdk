package com.post_sdk.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class OtpVerificationResponse (
    var success: Boolean,
    var status_code: Long,
    var error: String,
    var data: OtpVerificationData
): Parcelable {
    @Parcelize
    data class  OtpVerificationData(
        var user:OtpVerificationUserData,
        var customForm: OtpVerificationCustomForm
    ): Parcelable {
        @Parcelize
        data class OtpVerificationUserData(
            var id:String,
            var firstName:String?=null,
            var lastName:String?=null,
            var dob:String?=null,
            var gender:String?=null,
            var isActive:String?=null,
            var username:String,
            var addresses:ArrayList<OtpAddressObject>,
        ): Parcelable{
            @Parcelize
            data class OtpAddressObject(
                var _id:String,
                var address1:String,
                var address2:String,
                var landmark:String,
                var state:String,
                var city:String,
                var country:String,
                var pinCode:String,
                var isDefault:Boolean,
                var isActive:Boolean,
                var type:String?=null,
                var selected:Boolean=false,
            ):Parcelable
        }


        @Parcelize
        data class OtpVerificationCustomForm(
            var formName:String?=null,
            var brandName:String?=null,
            var brandLogo:String?=null,
            var backgroundColor:String?=null,
            var backgroundImage:String?=null,
            var brandLogoBorderColor:String?=null,
            var brandNameColor:String?=null,
            var buttonColor:String?=null,
            var buttonTextColor:String?=null,
            var fieldBorderColor:String?=null,
            var fieldLabelColor:String?=null,
            var fieldTextColor:String?=null,
            var customForm: ArrayList<OtpVerificationCustomFormInner>,
        ) : Parcelable {
            @Parcelize
            data class OtpVerificationCustomFormInner(
               var key:String,
               var skipAble:Boolean,
               var fields:ArrayList<CustomFormFields>
            ): Parcelable {
                @Parcelize
                data class CustomFormFields(
                    var key:String,
                    var type:String,
                    var value:String?=null,
                    var label:String,
                    var placeholder:String,
                    var required:Boolean,
                    var validations:ArrayList<CustomFormValidation>?=null,
                    var options:ArrayList<CustomFormOptions>?=null

                ): Parcelable{
                    @Parcelize
                    data class CustomFormOptions(
                        var key:String,
                        var label:String,
                    ):Parcelable

                    @Parcelize
                    data class  CustomFormValidation(
                        var required:Boolean?=null,
                        var min:Int?=null,
                        var max:Int?=null,
                    ):Parcelable
                }
            }
        }
    }
}
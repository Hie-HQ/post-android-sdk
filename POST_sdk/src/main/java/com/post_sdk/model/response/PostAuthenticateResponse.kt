package com.post_sdk.model.response

import java.io.Serializable

class PostAuthenticateResponse (
    var success: Boolean,
    var status_code: Long,
    var error: String,
    var data: PostAuthenticateData
        ):Serializable{
    inner class  PostAuthenticateData(
        var company: PostAuthenticateCompany,
        var tokens: PostAuthenticateTokens,
        var customForm: PostAuthenticateCustomForm
    ): Serializable{
        inner class PostAuthenticateCompany(
            var _id:String,
            var name:String,
            var isApproved:Boolean,
            var companyEmployees:ArrayList<String>,
            var companySecrets:ArrayList<String>,
        ): Serializable

        inner class PostAuthenticateTokens(
            var access: PostAuthenticateTokensData,
            var refresh: PostAuthenticateTokensData,
        ): Serializable
        {
            inner class PostAuthenticateTokensData(
                var token:String,
                var expires:String            ):Serializable
        }

        inner class PostAuthenticateCustomForm(
            var formName:String,
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
        ) : Serializable {

        }
    }
}
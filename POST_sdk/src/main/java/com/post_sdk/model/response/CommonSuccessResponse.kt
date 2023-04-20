package com.post_sdk.model.response

class CommonSuccessResponse(
    var success: Boolean,
    var status_code: Long,
    var error: String,
    var data: CommonSuccessData
) {
    inner class  CommonSuccessData(
        var message: String
            )
}
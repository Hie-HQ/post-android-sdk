package com.post_sdk.utils.ktx

import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle?.getParcelableOrNull(key: String): T? =
    this?.getParcelable(key) ?: null
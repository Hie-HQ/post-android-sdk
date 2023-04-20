package com.post_sdk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.post_sdk.BuildConfig
import com.post_sdk.ui.theme.gainsboro

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircleImage(url: String,size:Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(Color.Transparent, CircleShape)
    ) {
        GlideImage(
            model = BuildConfig.S3_BASE_URL+url,
            contentDescription ="",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
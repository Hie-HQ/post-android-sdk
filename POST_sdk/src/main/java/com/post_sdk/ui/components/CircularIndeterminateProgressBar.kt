package com.post_sdk.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean, verticalBias: Float,alpha:Float) {
    if (isDisplayed) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = alpha)),
        ) {
            val (progressBar) = createRefs()
            val topBias = createGuidelineFromTop(verticalBias)
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar)
                {
                    top.linkTo(topBias)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
                color = MaterialTheme.colors.primary
            )
        }

    }
}
package com.post_sdk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.post_sdk.R
import com.post_sdk.model.response.PostAuthenticateResponse


@Composable
fun AppBarWithBack(brandLogo:String?,brandName:String?)
{
    TopAppBar(
        modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 45.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = null // decorative element
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (brandLogo!=null)
                        {
                            CircleImage(url = brandLogo, size = 70)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        else if (brandName!=null)
                        {
                            Text(text = brandName,
                                color= Color.Black,
                                modifier = Modifier.wrapContentWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { /* do something */ },
                    modifier = Modifier
                        .padding(0.dp)
                        .alpha(0f)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = null // decorative element
                    )
                }
            }
        }
    )
}
package com.ulyup.tier_list.core.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.ulyup.tier_list.core.ui.token.roundedShape4
import com.ulyup.tier_list.core.ui.token.size20
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.ic_delete
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeleteButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size20)
            .clip(roundedShape4)
            .background(appColors.error)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_delete),
            contentDescription = contentDescription,
            tint = appColors.onError,
            modifier = Modifier.fillMaxSize(fraction = 0.7f),
        )
    }
}

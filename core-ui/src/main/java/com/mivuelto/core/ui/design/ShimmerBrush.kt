package com.mivuelto.core.ui.design

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun shimmerBrush(
    show: Boolean = true,
    targetValue: Float = 1000f,
    color: Color = Color.LightGray,
): Brush {
    return if (show) {
        val shimmerColors = listOf(
            color.copy(alpha = 0.6f),
            color.copy(alpha = 0.2f),
            color.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent,Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@SuppressLint("SuspiciousModifierThen")
fun Modifier.shimmer(
    show: Boolean = true,
    corner: Dp = 16.dp,
    color: Color = Color.LightGray
) = composed { this.then(
    background(
        brush = shimmerBrush(show = show, color = color),
        shape = RoundedCornerShape(corner),
    ).alpha(if(show)0f else 1f)
) }


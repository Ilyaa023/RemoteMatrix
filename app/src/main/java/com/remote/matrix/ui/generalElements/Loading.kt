package com.remote.matrix.ui.generalElements

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.remote.matrix.ui.theme.TertiaryDark
import com.remote.matrix.ui.theme.TertiaryLight
import kotlin.math.PI
import kotlin.math.sin

@Preview(showBackground = true)
@Composable
fun LoadingCube(
        count: Int = 10,
        size: Dp = 320.dp,
        color: Color = if(isSystemInDarkTheme()) TertiaryDark else TertiaryLight,
        backgroundColor: Color = MaterialTheme.colors.background
) {
    val transitionX = rememberInfiniteTransition()
    val currentXPhase by transitionX.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(animation = tween(
                durationMillis = 735,
                easing = LinearEasing
        ))
    )
    val transitionY = rememberInfiniteTransition()
    val currentYPhase by transitionY.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(animation = tween(
                durationMillis = 6000,
                easing = LinearEasing
        ))
    )

    Canvas(
        Modifier
            .progressSemantics()
            .size(size)
            .background(backgroundColor, RoundedCornerShape(20.dp))
    ) {
        val circleRadius = size.toPx() / count / 2
        val offset = Offset(
            (size.toPx() - (circleRadius * (count)) * 2 * 0.8f) / 2,
            (size.toPx() - (circleRadius * (count)) * 2 * 0.8f) / 2
        )
        for (w in 1..count)
            for (h in 1..count)
                drawCircle(color = color,
                           center = Offset(offset.x + (w * 2 - 1) * circleRadius * 0.8f,
                                           offset.y + (h * 2 - 1) * circleRadius * 0.8f),
                           radius = (circleRadius * 0.8f + circleRadius * 0.5f *
                                   sin((currentXPhase + 150 / count * w) * PI / 180) *
                                   sin((currentYPhase + 150 / count * h) * PI / 180)).toFloat())
    }
}
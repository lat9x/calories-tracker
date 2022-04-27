package com.tuan.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tuan.core.R
import com.tuan.tracker_presentation.components.UnitDisplay
import com.tuan.tracker_presentation.tracker_overview.util.Constants.ANIMATE_DURATION_TIME
import com.tuan.tracker_presentation.tracker_overview.util.Constants.INITIAL_ANIMATE_VALUE
import com.tuan.tracker_presentation.tracker_overview.util.Constants.SIX_OCLOCK
import com.tuan.tracker_presentation.tracker_overview.util.Constants.SQUARE_SHAPE_RATIO
import com.tuan.tracker_presentation.tracker_overview.util.Constants.THREE_OCLOCK
import com.tuan.tracker_presentation.tracker_overview.util.Constants.WHOLE_CIRCLE

/**
 * Circle shape that display the actual amount of either carbs, protein, or fat amount
 *
 * @param modifier custom modifier that can be passed from outside
 * @param value the amount that user has consumed
 * @param goal user's goal (either carbsGoal, proteinGoal, or fatGoal)
 * @param name name of the nutriment (can be either Carbs, Protein or Fat)
 * @param color the nutriment color to apply to the circle border color
 * @param strokeWidth how thick the circle should be
 */
@Composable
fun NutrientBarInfo(
    modifier: Modifier = Modifier,
    value: Int,
    goal: Int,
    name: String,
    color: Color,
    strokeWidth: Dp = 8.dp,
) {

    val backgroundColor: Color = MaterialTheme.colors.background
    val goalExceededColor: Color = MaterialTheme.colors.error

    // set the initial animatable value
    val angleRatio = remember {
        Animatable(initialValue = INITIAL_ANIMATE_VALUE)
    }

    LaunchedEffect(key1 = value) {
        angleRatio.animateTo(
            targetValue = if (goal > 0) {
                value / goal.toFloat()
            } else {
                INITIAL_ANIMATE_VALUE
            },
            animationSpec = tween(
                durationMillis = ANIMATE_DURATION_TIME
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // ratio = 1f to make it square
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = SQUARE_SHAPE_RATIO),
        ) {
            // fill the circle with default color
            drawArc(
                color = if (value <= goal) backgroundColor else goalExceededColor,
                startAngle = THREE_OCLOCK,
                sweepAngle = WHOLE_CIRCLE,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
            // fill the circle with the value's color if not exceed the goal
            if (value <= goal) {
                drawArc(
                    color = color,
                    startAngle = SIX_OCLOCK,
                    sweepAngle = WHOLE_CIRCLE * angleRatio.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UnitDisplay(
                amount = value,
                unit = stringResource(id = R.string.grams),
                amountColor = if (value <= goal)
                    MaterialTheme.colors.onPrimary
                else
                    goalExceededColor,
                unitColor = if (value <= goal)
                    MaterialTheme.colors.onPrimary
                else
                    goalExceededColor
            )
            Text(
                text = name,
                color = if (value <= goal)
                    MaterialTheme.colors.onPrimary
                else
                    goalExceededColor,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light
            )
        }
    }
}
package com.tuan.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.tuan.core_ui.CarbColor
import com.tuan.core_ui.FatColor
import com.tuan.core_ui.ProteinColor
import com.tuan.tracker_domain.util.Constants.CARBS_TO_CALORIES
import com.tuan.tracker_domain.util.Constants.FAT_TO_CALORIES
import com.tuan.tracker_domain.util.Constants.PROTEIN_TO_CALORIES
import com.tuan.tracker_presentation.tracker_overview.util.Constants.NUTRIMENTS_BAR_CORNER_RADIUS
import com.tuan.tracker_presentation.tracker_overview.util.Constants.INITIAL_ANIMATE_VALUE

/**
 * Nutrients Bar that displays the amount of carbs, protein and fat which the user has consumed
 * so far
 *
 * @param modifier custom modifier that can be passed from outside
 * @param carbs how much carbs has the user has consumed
 * @param protein how much protein has the user has consumed
 * @param fat how much carbs has the user has consumed
 * @param calories how much calories has the user has consumed
 * @param caloriesGoal the calorie goal
 */
@Composable
fun NutrientsBar(
    modifier: Modifier = Modifier,
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    caloriesGoal: Int
) {
    val backgroundColor: Color = MaterialTheme.colors.background
    val caloriesExceedColor: Color = MaterialTheme.colors.error

    // set the initial animatable value to 0 (no animation yet)
    val carbWidthRatio = remember {
        Animatable(initialValue = INITIAL_ANIMATE_VALUE)
    }
    val proteinWidthRatio = remember {
        Animatable(initialValue = INITIAL_ANIMATE_VALUE)
    }
    val fatWidthRatio = remember {
        Animatable(initialValue = INITIAL_ANIMATE_VALUE)
    }

    // start the animation as soon as this function gets composed
    LaunchedEffect(key1 = carbs) {
        carbWidthRatio.animateTo(
            targetValue = ((carbs * CARBS_TO_CALORIES) / caloriesGoal)
        )
    }
    LaunchedEffect(key1 = protein) {
        proteinWidthRatio.animateTo(
            targetValue = ((protein * PROTEIN_TO_CALORIES) / caloriesGoal)
        )
    }
    LaunchedEffect(key1 = fat) {
        fatWidthRatio.animateTo(
            targetValue = ((fat * FAT_TO_CALORIES) / caloriesGoal)
        )
    }

    Canvas(modifier = modifier) {
        // amount of consumed calories have not exceeded the goal yet
        if (calories <= caloriesGoal) {
            // get the actual width of each nutriment
            val carbsWidth = carbWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            // draw the horizontal bar
            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(x = NUTRIMENTS_BAR_CORNER_RADIUS)
            )

            // draw fat nutriment (overall width = carbsWidth + proteinWidth + fatWidth)
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(x = NUTRIMENTS_BAR_CORNER_RADIUS)
            )

            // draw protein nutriment (overall width = carbsWidth + proteinWidth)
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(x = NUTRIMENTS_BAR_CORNER_RADIUS)
            )

            // draw carbs nutriment
            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(x = NUTRIMENTS_BAR_CORNER_RADIUS)
            )
        } else {
            // amount of consumed calories exceeds the goal
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = CornerRadius(x = NUTRIMENTS_BAR_CORNER_RADIUS)
            )
        }
    }
}
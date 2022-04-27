package com.tuan.onboarding_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tuan.core_ui.CornerRadius
import com.tuan.core_ui.LocalCornerRadius
import com.tuan.core_ui.LocalSpacing
import com.tuan.core_ui.Spacing

/**
 * A custom rounded corner box that behaves like Button
 *
 * @param modifier modifier that can be passed from the outside
 * @param color the box's background color
 * @param selectedTextColor text color when the box is selected
 * @param text the text inside the box
 * @param isSelected is the box selected or not
 * @param onClick handles on click event
 * @param textStyle style of the text inside
 */
@Composable
fun SelectableButton(
    modifier: Modifier = Modifier,
    color: Color,
    selectedTextColor: Color,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.button
) {

    val spacing: Spacing = LocalSpacing.current
    val cornerRadius: CornerRadius = LocalCornerRadius.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = cornerRadius.radiusExtraLarge))
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(size = cornerRadius.radiusExtraLarge)
            )
            .background(
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(size = cornerRadius.radiusExtraLarge)
            )
            .clickable {
                onClick()
            }
            .padding(all = spacing.spaceMedium),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isSelected) selectedTextColor else color,
        )
    }
}
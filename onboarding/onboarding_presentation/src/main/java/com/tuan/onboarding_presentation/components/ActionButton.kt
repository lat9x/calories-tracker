package com.tuan.onboarding_presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tuan.core_ui.CornerRadius
import com.tuan.core_ui.LocalCornerRadius
import com.tuan.core_ui.LocalSpacing
import com.tuan.core_ui.Spacing

/**
 * Custom button
 *
 * @param modifier custom modifier that is passed from outside
 * @param text the button text
 * @param onClick onClick action
 * @param isEnabled is the button enabled or not (enable by default)
 * @param textStyle style of the button text
 */
@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.button,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val cornerRadius: CornerRadius = LocalCornerRadius.current
    val spacing: Spacing = LocalSpacing.current

    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(size = cornerRadius.radiusExtraLarge)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(all = spacing.spaceSmall)
        )
    }
}
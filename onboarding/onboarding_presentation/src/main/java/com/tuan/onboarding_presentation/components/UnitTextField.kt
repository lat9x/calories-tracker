package com.tuan.onboarding_presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.tuan.core_ui.LocalSpacing

/**
 * Custom TextField that contains a big text (called value) and a small text (the value's unit).
 * Example: 20kg, 180cm, etc.
 *
 * @param modifier modifier that can be passed from the outside
 * @param value the big text
 * @param onValueChange handles value change event
 * @param unit the value's unit
 * @param textStyle style of the big text
 * @param onDoneClick what to do when user click the imeAction (do nothing by default)
 */
@Composable
fun UnitTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    unit: String,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colors.primaryVariant,
        fontSize = 60.sp
    ),
    onDoneClick: () -> Unit = {}
) {

    val spacing = LocalSpacing.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDoneClick()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Min)
                .alignBy(alignmentLine = LastBaseline)
        )
        Spacer(modifier = Modifier.width(width = spacing.spaceSmall))
        Text(
            text = unit,
            modifier = Modifier.alignBy(alignmentLine = LastBaseline)
        )
    }
}
// what is width(IntrinsicSize.Min): dynamically adjust the text width (like wrap-content in XML)
// what is LastBaseline: align big text and small text on the same line
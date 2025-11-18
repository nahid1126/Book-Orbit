package com.nahid.book_orbit.ui.presentation.component.input_field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.core.utils.AppConstants
import com.suffixit.smartadmin.core.utils.extension.checkInputType
import com.suffixit.smartadmin.core.utils.extension.equalIgnoreCase

@Composable
fun InputOutlinedTextComponent(question: InputQuestion, onAnswerChange: (String) -> Unit) {
    val modifier = if (question.inputFieldSize.equalIgnoreCase("LARGE")) {
        Modifier.fillMaxWidth()
    } else if (question.inputFieldSize.equalIgnoreCase("MEDIUM")) {
        Modifier.fillMaxWidth(0.7f)
    } else {
        Modifier.fillMaxWidth(0.5f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth().padding(top = AppConstants.APP_MARGIN.dp).padding(horizontal = AppConstants.APP_MARGIN.dp)
    ) {

        val inputType = when (question.inputType) {
            "ONLY_NUMBER" -> KeyboardType.Number
            "DECIMAL_NUMBER" -> KeyboardType.Number
            "PASSWORD" -> KeyboardType.Password
            else -> KeyboardType.Text
        }

        if (question.questionText.isNotEmpty()) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding((AppConstants.APP_MARGIN/2).dp))
        }

        var text by rememberSaveable { mutableStateOf(if (question.textInput.isNotEmpty()) question.textInput else "") }

        OutlinedTextField(
            value = text,
            onValueChange = {
                if (question.textLimit >= it.length && it.checkInputType(question.questionText)
                ) {
                    text = it
                    onAnswerChange(text)
                }
            },
            label = {
                Text(question.textHint)
            },
            keyboardOptions = KeyboardOptions(keyboardType = inputType),
            colors = outlinedTextFieldColors(),
            singleLine = question.minLines == 1,
            minLines = question.minLines,
            modifier = modifier,
        )
    }




}

@Composable
fun outlinedTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.tertiary,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )
}




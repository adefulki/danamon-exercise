package ade.test.danamon.ui.components

import ade.test.danamon.R
import ade.test.danamon.ui.utils.TextFieldState
import ade.test.danamon.ui.utils.UsernameState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun Username(
    usernameState: TextFieldState = remember { UsernameState() },
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = usernameState.text,
        onValueChange = {
            usernameState.text = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.username),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                usernameState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    usernameState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        isError = usernameState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
        singleLine = true
    )

    usernameState.getError()?.let { error -> TextFieldError(textError = error) }
}
package ade.test.danamon.ui.components

import ade.test.danamon.R
import ade.test.danamon.ui.signup.Role
import ade.test.danamon.ui.utils.TextFieldState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Role(
    roleState: TextFieldState = remember { TextFieldState() },
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val roles = arrayOf(Role.Admin, Role.User)
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.width((configuration.screenWidthDp / 2).dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = roleState.text,
                onValueChange = {
                    roleState.text = it
                },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = {
                    Text(
                        text = stringResource(id = R.string.role),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = modifier
                    .menuAnchor()
                    .onFocusChanged { focusState ->
                        roleState.onFocusChange(focusState.isFocused)
                        if (!focusState.isFocused) {
                            roleState.enableShowErrors()
                        } else {
                            expanded = true
                        }
                    },
                isError = roleState.showErrors(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onImeAction()
                    }
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            roleState.text = item.name
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
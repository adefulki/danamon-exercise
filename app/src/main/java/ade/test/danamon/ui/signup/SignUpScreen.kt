package ade.test.danamon.ui.signup

import ade.test.danamon.R
import ade.test.danamon.ui.theme.ExerciseTheme
import ade.test.danamon.ui.theme.stronglyDeemphasizedAlpha
import ade.test.danamon.ui.utils.ConfirmPasswordState
import ade.test.danamon.ui.utils.EmailState
import ade.test.danamon.ui.utils.PasswordState
import ade.test.danamon.ui.utils.supportWideScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ade.test.danamon.ui.components.Email
import ade.test.danamon.ui.components.Password
import ade.test.danamon.ui.components.Role
import ade.test.danamon.ui.components.SignInSignUpScreen
import ade.test.danamon.ui.components.TopAppBar
import ade.test.danamon.ui.components.Username
import ade.test.danamon.ui.utils.TextFieldState
import ade.test.danamon.ui.utils.UsernameState
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    email: String?,
    onSignUpSubmitted: (String) -> Unit,
    onSignIn: () -> Unit,
    onNavUp: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val eventFlow = viewModel.eventFlow
    val successStr = stringResource(id = R.string.sign_up_success)

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is SignUpViewModel.UIEvent.RegisterUser -> {
                    if (event.isSuccess) {
                        Toast.makeText(context, successStr, Toast.LENGTH_SHORT).show()
                        onSignUpSubmitted(event.email.orEmpty())
                    } else {
                        Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                topAppBarText = stringResource(id = R.string.create_account),
                onNavUp = onNavUp,
            )
        },
        content = { contentPadding ->
            SignInSignUpScreen(
                isSignIn = false,
                action = onSignIn,
                contentPadding = contentPadding,
                modifier = Modifier.supportWideScreen()
            ) {
                Column {
                    SignUpContent(
                        email = email,
                        onSignUpSubmitted = { email, username, password, role ->
                            viewModel.register(email, username, password, Role.valueOf(role))
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SignUpContent(
    email: String?,
    onSignUpSubmitted: (email: String, username: String, password: String, role: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val usernameFocusRequest = remember { FocusRequester() }
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val roleFocusRequest = remember { FocusRequester() }

        val emailState = remember { EmailState(email) }
        Email(emailState, onImeAction = { usernameFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))
        val usernameState = remember { UsernameState() }
        Username(
            usernameState,
            onImeAction = { passwordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(usernameFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )

        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
        Password(
            label = stringResource(id = R.string.confirm_password),
            passwordState = confirmPasswordState,
            imeAction = ImeAction.Next,
            onImeAction = { roleFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        val roleState = remember { TextFieldState() }

        roleState.text = Role.Admin.name
        Role(
            roleState = roleState,
            imeAction = ImeAction.Done,
            onImeAction = {
                onSignUpSubmitted(
                    emailState.text,
                    usernameState.text,
                    passwordState.text,
                    roleState.text
                )
            },
            modifier = Modifier.focusRequester(roleFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.terms_and_conditions),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onSignUpSubmitted(
                    emailState.text,
                    usernameState.text,
                    passwordState.text,
                    roleState.text
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid &&
                    passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(text = stringResource(id = R.string.create_account))
        }
    }
}

@Preview(widthDp = 1024)
@Composable
fun SignUpPreview() {
    ExerciseTheme {
        SignUpScreen(
            email = null,
            onSignUpSubmitted = { },
            onSignIn = {},
            onNavUp = {},
        )
    }
}

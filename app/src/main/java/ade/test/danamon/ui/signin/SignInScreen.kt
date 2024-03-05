package ade.test.danamon.ui.signin

import ade.test.danamon.R
import ade.test.danamon.data.pref.UserPref
import ade.test.danamon.ui.components.Email
import ade.test.danamon.ui.components.Password
import ade.test.danamon.ui.components.SignInSignUpScreen
import ade.test.danamon.ui.components.TopAppBar
import ade.test.danamon.ui.theme.ExerciseTheme
import ade.test.danamon.ui.utils.EmailState
import ade.test.danamon.ui.utils.EmailStateSaver
import ade.test.danamon.ui.utils.PasswordState
import ade.test.danamon.ui.utils.supportWideScreen
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    email: String?,
    onSignInSubmitted: () -> Unit,
    onSignUp: () -> Unit,
    onNavUp: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val eventFlow = viewModel.eventFlow
    val successStr = stringResource(id = R.string.sign_in_success)

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is SignInViewModel.UIEvent.LoginUser -> {
                    if (event.isSuccess) {
                        UserPref(context).user = event.user
                        Toast.makeText(context, successStr, Toast.LENGTH_SHORT).show()
                        onSignInSubmitted()
                    } else {
                        Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val snackbarErrorText = stringResource(id = R.string.feature_not_available)
    val snackbarActionLabel = stringResource(id = R.string.dismiss)

    Scaffold(
        topBar = {
            TopAppBar(
                topAppBarText = stringResource(id = R.string.sign_in),
                onNavUp = onNavUp,
            )
        },
        content = { contentPadding ->
            SignInSignUpScreen(
                isSignIn = true,
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding,
                action = onSignUp,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SignInContent(
                        email = email,
                        onSignInSubmitted = { email, password -> viewModel.login(email, password) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarErrorText,
                                    actionLabel = snackbarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.forgot_password))
                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackbar(
            snackbarHostState = snackbarHostState,
            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SignInContent(
    email: String?,
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
            mutableStateOf(EmailState(email))
        }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }

        val onSubmit = {
            if (emailState.isValid && passwordState.isValid) {
                onSignInSubmitted(emailState.text, passwordState.text)
            }
        }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSubmit() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}

@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                action = {
                    data.visuals.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

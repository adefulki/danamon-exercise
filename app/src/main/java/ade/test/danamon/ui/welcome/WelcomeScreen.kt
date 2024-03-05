package ade.test.danamon.ui.welcome

import ade.test.danamon.R
import ade.test.danamon.ui.components.Email
import ade.test.danamon.ui.components.OrSignUp
import ade.test.danamon.ui.theme.ExerciseTheme
import ade.test.danamon.ui.theme.stronglyDeemphasizedAlpha
import ade.test.danamon.ui.utils.EmailState
import ade.test.danamon.ui.utils.EmailStateSaver
import ade.test.danamon.ui.utils.supportWideScreen
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WelcomeScreen(
    onContinue: (email: String, isExist: Boolean) -> Unit,
    onSignUp: () -> Unit,
    onAlreadyLogin: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    var showBranding by rememberSaveable { mutableStateOf(true) }

    val eventFlow = viewModel.eventFlow
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is WelcomeViewModel.UIEvent.FindEmail -> {
                    onContinue(event.email, event.isExist)
                }
            }
        }
    }

    Scaffold(modifier = Modifier.supportWideScreen()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(
                showBranding,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp)
            ) {
                Branding()
            }

            SignInCreateAccount(
                onSignInSignUp = onContinue,
                onSignInAsGuest = onSignUp,
                onFocusChange = { focused -> showBranding = !focused },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                viewModel
            )
        }
    }
}

@Composable
private fun Branding(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.app_tagline),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = LocalContentColor.current.luminance() < 0.5f,
) {
    val assetId = if (lightTheme) {
        R.drawable.bank_danamon_logo
    } else {
        R.drawable.bank_danamon_logo
    }
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = stringResource(id = R.string.image_welcome)
    )
}

@Composable
private fun SignInCreateAccount(
    onSignInSignUp: (email: String, isExist: Boolean) -> Unit,
    onSignInAsGuest: () -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel
) {
    val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
        mutableStateOf(EmailState())
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.sign_in_create_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
        )
        val onSubmit = {
            if (emailState.isValid) {
                viewModel.isEmailExist(emailState.text)
            } else {
                emailState.enableShowErrors()
            }
        }
        onFocusChange(emailState.isFocused)
        Email(emailState = emailState, imeAction = ImeAction.Done, onImeAction = onSubmit)
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_continue),
                style = MaterialTheme.typography.titleSmall
            )
        }
        OrSignUp(
            onSignUp = onSignInAsGuest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
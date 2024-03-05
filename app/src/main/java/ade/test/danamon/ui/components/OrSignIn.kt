package ade.test.danamon.ui.components

import ade.test.danamon.R
import ade.test.danamon.ui.theme.ExerciseTheme
import ade.test.danamon.ui.theme.stronglyDeemphasizedAlpha
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrSignIn(
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            modifier = Modifier.paddingFromBaseline(top = 25.dp)
        )
        OutlinedButton(
            onClick = onSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 24.dp),
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }
    }
}

@Preview
@Composable
fun OrSignInPreview() {
    ExerciseTheme {
        Surface {
            OrSignIn(
                onSignIn = {}
            )
        }
    }
}

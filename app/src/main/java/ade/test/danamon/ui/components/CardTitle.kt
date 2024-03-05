package ade.test.danamon.ui.components

import ade.test.danamon.domain.model.User
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CardTitle(user: User) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = user.id,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp, start = 4.dp, end = 4.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = user.role,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
        )
    }
}
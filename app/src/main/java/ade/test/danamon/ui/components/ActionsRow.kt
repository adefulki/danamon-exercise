package ade.test.danamon.ui.components

import ade.test.danamon.R
import ade.test.danamon.domain.model.User
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ActionsRow(
    user: User,
    actionIconSize: Dp,
    cardHeight: Dp,
    onDelete: (User) -> Unit,
    onEdit: (User) -> Unit
) {
    Column(
        Modifier
            .padding(vertical = 8.dp)
            .height(cardHeight)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = { onDelete(user) },
            content = {
                Icon(
                    Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = stringResource(id = R.string.delete_action),
                )
            }
        )
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = { onEdit(user) },
            content = {
                Icon(
                    Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = stringResource(id = R.string.edit_action),
                )
            },
        )
    }
}
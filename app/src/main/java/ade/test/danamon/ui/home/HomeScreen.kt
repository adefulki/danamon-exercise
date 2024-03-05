package ade.test.danamon.ui.home

import ade.test.danamon.R
import ade.test.danamon.data.pref.UserPref
import ade.test.danamon.domain.model.Photo
import ade.test.danamon.domain.model.User
import ade.test.danamon.ui.components.ActionsRow
import ade.test.danamon.ui.components.DraggableCard
import ade.test.danamon.ui.components.Email
import ade.test.danamon.ui.components.ErrorMessage
import ade.test.danamon.ui.components.ItemPhoto
import ade.test.danamon.ui.components.LoadingNextPageItem
import ade.test.danamon.ui.components.PageLoader
import ade.test.danamon.ui.components.Password
import ade.test.danamon.ui.components.Role
import ade.test.danamon.ui.components.Username
import ade.test.danamon.ui.components.appbar.AppBar
import ade.test.danamon.ui.components.appdrawer.AppDrawerContent
import ade.test.danamon.ui.signup.Role
import ade.test.danamon.ui.theme.stronglyDeemphasizedAlpha
import ade.test.danamon.ui.utils.ConfirmPasswordState
import ade.test.danamon.ui.utils.EmailState
import ade.test.danamon.ui.utils.PasswordState
import ade.test.danamon.ui.utils.TextFieldState
import ade.test.danamon.ui.utils.UsernameState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Surface {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    drawerState = drawerState,
                    onLogout = onLogout
                )
            }
        ) {
            Scaffold(
                topBar = {
                    AppBar(drawerState = drawerState)
                }
            ) {
                when (UserPref(context).user?.role) {
                    Role.Admin.name -> {
                        viewModel.getUsers()
                        AdminHomeScreen(paddingValues = it, viewModel = viewModel)
                    }

                    Role.User.name -> {
                        viewModel.getPhotos()
                        UserHomeScreen(paddingValues = it, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun UserHomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel
) {
    val photoPagingItems: LazyPagingItems<Photo> = viewModel.photoState.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        item { Spacer(modifier = Modifier.padding(4.dp)) }
        items(photoPagingItems.itemCount) { index ->
            ItemPhoto(
                itemEntity = photoPagingItems[index]!!,
                onClick = {

                }
            )
        }
        photoPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = photoPagingItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillParentMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item { LoadingNextPageItem(modifier = Modifier) }
                }

                loadState.append is LoadState.Error -> {
                    val error = photoPagingItems.loadState.append as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier,
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.padding(4.dp)) }
    }
}

@Composable
fun AdminHomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel
) {
    val users by viewModel.usersState.collectAsStateWithLifecycle()
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsStateWithLifecycle()

    var showEditSheet by remember { mutableStateOf(false) }
    var showDeleteSheet by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf(User("", "", "", "", "", "")) }

    if (showEditSheet) {
        BottomSheetEdit(selectedUser, viewModel) {
            showEditSheet = false
        }
    }

    if (showDeleteSheet) {
        BottomSheetDelete(selectedUser, viewModel) {
            showDeleteSheet = false
        }
    }

    LazyColumn(Modifier.padding(paddingValues)) {
        items(users.size) { position ->
            val user = users[position]
            Box(Modifier.fillMaxWidth()) {
                ActionsRow(
                    user = user,
                    actionIconSize = 56.dp,
                    cardHeight = 120.dp,
                    onDelete = {
                        selectedUser = it
                        showDeleteSheet = true
                    },
                    onEdit = {
                        selectedUser = it
                        showEditSheet = true
                    }
                )

                DraggableCard(
                    user = user,
                    isRevealed = revealedCardIds.contains(user.id),
                    cardHeight = 120.dp,
                    cardOffset = 150f,
                    onExpand = { viewModel.onItemExpanded(user.id) },
                    onCollapse = { viewModel.onItemCollapsed(user.id) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetEdit(user: User, viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            val usernameFocusRequest = remember { FocusRequester() }
            val passwordFocusRequest = remember { FocusRequester() }
            val confirmationPasswordFocusRequest = remember { FocusRequester() }
            val roleFocusRequest = remember { FocusRequester() }

            val emailState = remember { EmailState(user.email) }
            val usernameState = remember { UsernameState(user.username) }
            val passwordState = remember { PasswordState(user.password) }
            val confirmPasswordState =
                remember { ConfirmPasswordState(passwordState = passwordState) }
            val roleState = remember { TextFieldState() }
            roleState.text = user.role

            val onSubmit = {
                if (emailState.isValid && usernameState.isValid &&
                    passwordState.isValid && confirmPasswordState.isValid
                ) {
                    val editedUser = ade.test.danamon.data.local.model.User()
                    editedUser.apply {
                        id = user.id
                        email = emailState.text
                        username = usernameState.text
                        password = passwordState.text
                        role = roleState.text
                    }
                    viewModel.editUser(editedUser)
//                    scope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
//                        if (!modalBottomSheetState.isVisible) {
//                            showBottomSheet = false
//                        }
//                    }
                }
            }

            Email(emailState, onImeAction = { usernameFocusRequest.requestFocus() })
            Spacer(modifier = Modifier.height(16.dp))
            Username(
                usernameState,
                onImeAction = { passwordFocusRequest.requestFocus() },
                modifier = Modifier.focusRequester(usernameFocusRequest)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Password(
                label = stringResource(id = R.string.password),
                passwordState = passwordState,
                imeAction = ImeAction.Next,
                onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                modifier = Modifier.focusRequester(passwordFocusRequest)
            )
            Password(
                label = stringResource(id = R.string.confirm_password),
                passwordState = confirmPasswordState,
                imeAction = ImeAction.Next,
                onImeAction = { roleFocusRequest.requestFocus() },
                modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
            )
            Role(
                roleState = roleState,
                imeAction = ImeAction.Done,
                onImeAction = {
                    onSubmit()
                },
                modifier = Modifier.focusRequester(roleFocusRequest)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                enabled = emailState.isValid && usernameState.isValid &&
                        passwordState.isValid && confirmPasswordState.isValid
            ) {
                Text(text = stringResource(id = R.string.update))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDelete(user: User, viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            val context = LocalContext.current

            val focusRequester = remember { FocusRequester() }

            val passwordState = remember { PasswordState() }
            passwordState.text = UserPref(context).user?.password.orEmpty()
            val confirmPasswordState = remember { ConfirmPasswordState(passwordState) }

            val onSubmit = {
                if (confirmPasswordState.isValid) {
                    viewModel.deleteUser(user)
//                    scope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
//                        if (!modalBottomSheetState.isVisible) {
//                            showBottomSheet = false
//                        }
//                    }
                }
            }

            Text(
                text = stringResource(id = R.string.update_confirmation),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Password(
                label = stringResource(id = R.string.password),
                modifier = Modifier.focusRequester(focusRequester),
                passwordState = confirmPasswordState,
                onImeAction = { onSubmit() }
            )
            Button(
                onClick = { onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 40.dp),
                enabled = confirmPasswordState.isValid
            ) {
                Text(
                    text = stringResource(id = R.string.delete)
                )
            }
        }
    }
}
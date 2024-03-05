package ade.test.danamon.ui.home

import ade.test.danamon.data.Result
import ade.test.danamon.domain.model.Photo
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.model.toModelUser
import ade.test.danamon.domain.usecase.DeleteUserUseCase
import ade.test.danamon.domain.usecase.EditUserUseCase
import ade.test.danamon.domain.usecase.GetPhotosUseCase
import ade.test.danamon.domain.usecase.GetUsersUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val editUserUseCase: EditUserUseCase
) : ViewModel() {

    private val _photoState: MutableStateFlow<PagingData<Photo>> =
        MutableStateFlow(value = PagingData.empty())
    val photoState: MutableStateFlow<PagingData<Photo>> get() = _photoState

    private val _usersState = MutableStateFlow(listOf<User>())
    val usersState: StateFlow<List<User>> get() = _usersState

    private val _revealedCardIdsList = MutableStateFlow(listOf<String>())
    val revealedCardIdsList: StateFlow<List<String>> get() = _revealedCardIdsList

    fun getPhotos() {
        viewModelScope.launch {
            getPhotosUseCase.execute(Unit)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _photoState.value = it
                }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            when (val result = getUsersUseCase.invoke()) {
                is Result.Success -> {
                    result.data?.let { _usersState.emit(it) }
                }

                is Result.Error -> {

                }

                is Result.Loading -> {

                }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            deleteUserUseCase.invoke(user.id)
            getUsers()
        }
    }

    fun editUser(user: ade.test.danamon.data.local.model.User) {
        viewModelScope.launch {
            when (val result = editUserUseCase.invoke(user)) {
                is Result.Success -> {
                    if (result.data == true) getUsers()
                }

                is Result.Error -> {

                }

                is Result.Loading -> {

                }
            }
        }
    }

    fun onItemExpanded(cardId: String) {
        if (_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.add(cardId)
        }
    }

    fun onItemCollapsed(cardId: String) {
        if (!_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.remove(cardId)
        }
    }
}
package ade.test.danamon.ui.signin

import ade.test.danamon.data.Result
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.usecase.LoginUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val result = loginUseCase.invoke(email, password)) {
                is Result.Success -> {
                    result.data?.let {
                        _eventFlow.emit(
                            UIEvent.LoginUser(
                                true,
                                it,
                                result.message.orEmpty()
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _eventFlow.emit(UIEvent.LoginUser(false, null, result.message.orEmpty()))
                }

                is Result.Loading -> {}
            }

        }
    }

    sealed class UIEvent {
        data class LoginUser(val isSuccess: Boolean, val user: User?, val errorMessage: String?) :
            UIEvent()
    }
}
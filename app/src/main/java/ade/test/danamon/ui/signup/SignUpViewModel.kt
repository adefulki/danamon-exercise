package ade.test.danamon.ui.signup

import ade.test.danamon.data.local.model.User
import ade.test.danamon.domain.usecase.RegisterUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun register(email: String, username: String, password: String, role: Role) {
        viewModelScope.launch {
            val user = User(UUID.randomUUID().toString(), email, username, password, role.name)
            val result = registerUseCase.invoke(user)
            result.data?.let {
                _eventFlow.emit(
                    UIEvent.RegisterUser(
                        it,
                        email,
                        result.message.orEmpty()
                    )
                )
            }
        }
    }

    sealed class UIEvent {
        data class RegisterUser(
            val isSuccess: Boolean,
            val email: String?,
            val errorMessage: String?
        ) : UIEvent()
    }

}

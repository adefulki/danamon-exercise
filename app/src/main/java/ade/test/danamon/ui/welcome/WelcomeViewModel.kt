package ade.test.danamon.ui.welcome

import ade.test.danamon.domain.usecase.FindEmailUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val findEmailUseCase: FindEmailUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun isEmailExist(email: String) {
        viewModelScope.launch {
            val result = findEmailUseCase.invoke(email)
            result.data?.let { _eventFlow.emit(UIEvent.FindEmail(email, it)) }
        }
    }

    sealed class UIEvent {
        data class FindEmail(val email: String, val isExist: Boolean) : UIEvent()
    }
}

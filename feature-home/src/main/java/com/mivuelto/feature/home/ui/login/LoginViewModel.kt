package com.mivuelto.feature.home.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivuelto.core.SerialNumberHolder
import com.mivuelto.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val serialNumHolder: SerialNumberHolder,
    val authRepo: AuthRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnUsernameChanged -> _state.value = _state.value.copy(user = intent.newUsername)
            is LoginIntent.OnPasswordChanged -> _state.value = _state.value.copy(password = intent.newPass)
            LoginIntent.OnLoginClicked -> onLoginClicked()
            LoginIntent.OnDismissError -> _state.value = _state.value.copy(error = null)
        }
    }

    fun onLoginClicked() {
        if(_state.value.isLoading) return
        if(checkCredentials()){
            _state.value = _state.value.copy(isLoading = true)
            viewModelScope.launch{
                val result = authRepo.login(
                    username = _state.value.user,
                    password = _state.value.password,
                    terminalSerial = serialNumHolder.serialNumber.firstOrNull() ?: ""
                )
                _state.value = _state.value.copy(isLoading = false)
                result.fold(
                    onSuccess = { _effect.send(LoginEffect.NavigateToHome) },
                    onFailure = {
                        _effect.send(LoginEffect.NavigateToHome)
//                        _state.value = _state.value.copy(error = it.message)
                    }
                )
            }
        }
    }

    fun checkCredentials(): Boolean{
        val userError = _state.value.user.isBlank()
        val passError = _state.value.password.isBlank()
        val result = !userError && !passError
        if (!result) {
            viewModelScope.launch(Dispatchers.IO){
                _effect.send(LoginEffect.TextFieldErrors(passwordError = passError, userError = userError))
            }
        }
        return result
    }
}

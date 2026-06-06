package com.mivuelto.feature.purchase.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val serialNum: String
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
            LoginIntent.OnDismissError -> Unit
        }
    }

    fun onLoginClicked() {
        if(checkCredentials()){
            viewModelScope.launch(Dispatchers.IO){
                _effect.send(LoginEffect.NavigateToHome)
            }
        }
    }

    fun checkCredentials(): Boolean{
        val userError = _state.value.user.isBlank()
        val passError = _state.value.password.isBlank()
        val result = !userError && !passError
            viewModelScope.launch(Dispatchers.IO){
                _effect.send(if(result){
                    LoginEffect.NavigateToHome
                }
                else{
                    LoginEffect.TextFieldErrors(passwordError = passError, userError = userError)
                })
            }
        return result
    }
}



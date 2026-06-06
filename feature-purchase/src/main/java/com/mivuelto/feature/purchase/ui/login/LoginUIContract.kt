package com.mivuelto.feature.purchase.ui.login


data class LoginState(
    val isLoading: Boolean = false,
    val user: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoginSuccess: Boolean = false
)

sealed interface LoginIntent {
    data class OnUsernameChanged(val newUsername: String) : LoginIntent
    data class OnPasswordChanged(val newPass: String) : LoginIntent
    object OnLoginClicked : LoginIntent
    object OnDismissError : LoginIntent
}

sealed interface LoginEffect {
    object NavigateToHome : LoginEffect
    data class ShowToast(val message: String) : LoginEffect
    data class TextFieldErrors(val userError: Boolean, val passwordError: Boolean) : LoginEffect
}



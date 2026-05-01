package com.mivuelto.core.ui.common

import com.mivuelto.core.ui.R

sealed interface UiEvent {
    data class TaskDone(val msg: String = "", val msgId: Int = 0) : UiEvent
    data class Error(
        val msg: String = "",
        val code: String = "0",
        val msgId: Int = 0,
        val withRetry: Boolean = true,
        val isBackToHome: Boolean = false,
    ) : UiEvent
    data class Loader(val isLoading: Boolean, val msg: Int = R.string.empty_value) : UiEvent
    data class Notification(val isLoading: Boolean, val msg: String = "") : UiEvent
    data class Toast(val msg: String = "", val msgId: Int = 0) : UiEvent
    data object PinRequested : UiEvent
    data class SelectAID(val apps: MutableList<String>?) : UiEvent
    data object GoToInvoice : UiEvent
    data object EnterRef : UiEvent
    data object VoidInvoice : UiEvent
    data object OnBack : UiEvent
    data object BackToHome : UiEvent
    data object NextScreen : UiEvent
    data object TimerFinished : UiEvent
    data class Dialog(val msg: String = "", val msgId: Int = 0, val event: UiEvent = NoEvent, val arg1: String = "") : UiEvent
    data class Progress(val isLoading: Boolean = true, val progress: Int) : UiEvent
    data class SnackBar(val msg: String = "", val msgId: Int = 0) : UiEvent

    data object LaunchPermRequest : UiEvent
    data object ReadAgain : UiEvent
    data object NoEvent : UiEvent

    data object WIFI_ON : UiEvent
    data object WIFI_OFF : UiEvent
}

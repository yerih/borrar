package com.mivuelto.feature.checkpayment.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.usecase.GetTransactionHistoryUseCase
import com.mivuelto.core.formatCents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject constructor(
    private val getHistory: GetTransactionHistoryUseCase,
) : ViewModel() {

    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg = ""

    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()

    fun loadHistory() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            getHistory().fold(
                onFailure = { e ->
                    errorMsg = e.message ?: "Unknown error"
                    isLoading = false
                    _effect.send(UiEvent.Error(msg = errorMsg))
                },
                onSuccess = { list ->
                    transactions = list
                    isLoading = false
                    _effect.send(UiEvent.TaskDone())
                }
            )
        }
    }

    fun formatAmount(cents: Long): String = cents.formatCents()
}

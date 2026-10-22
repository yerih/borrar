package com.mivuelto.feature.checkpayment.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.DetailedReportModel
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.usecase.GetTransactionHistoryUseCase
import com.mivuelto.core.domain.usecase.ObserveBanksUseCase
import com.mivuelto.core.domain.usecase.SyncBanksUseCase
import com.mivuelto.core.formatCents
import com.mivuelto.core.toDateTimeFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject constructor(
    private val getHistory: GetTransactionHistoryUseCase,
    private val observeBanks: ObserveBanksUseCase,
    private val syncBanks: SyncBanksUseCase,
) : ViewModel() {

    var items by mutableStateOf<List<DetailedReportModel>>(emptyList())
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
            val bankNames = loadBankNames()
            getHistory().fold(
                onFailure = { e ->
                    errorMsg = e.message ?: "Unknown error"
                    isLoading = false
                    _effect.send(UiEvent.Error(msg = errorMsg))
                },
                onSuccess = { transactions ->
                    items = transactions.map { it.toReport(bankNames) }
                    isLoading = false
                    _effect.send(UiEvent.TaskDone())
                }
            )
        }
    }

    private suspend fun loadBankNames(): Map<String, String> {
        val cached = observeBanks().first()
        val banks = if (cached.isNotEmpty()) cached else syncBanks().getOrElse { emptyList() }
        return banks.associate { it.uuid to it.name }
    }

    private fun Transaction.toReport(bankNames: Map<String, String>) = DetailedReportModel(
        reference = referenceNumber.orEmpty(),
        date = runCatching { created.toDateTimeFormatted() }.getOrElse { created },
        amount = amountCents.formatCents(),
        bank = fromBankId?.let { bankNames[it] }.orEmpty(),
        fromPhone = fromPhone.orEmpty()
    )
}

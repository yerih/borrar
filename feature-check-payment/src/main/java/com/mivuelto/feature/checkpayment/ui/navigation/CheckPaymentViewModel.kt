package com.mivuelto.feature.checkpayment.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionType
import com.mivuelto.core.domain.usecase.ObserveBanksUseCase
import com.mivuelto.core.domain.usecase.ObserveUserUseCase
import com.mivuelto.core.domain.usecase.QueryTransactionUseCase
import com.mivuelto.core.domain.usecase.SyncBanksUseCase
import com.mivuelto.core.formatDate
import com.mivuelto.core.log
import com.mivuelto.core.toDateFormatted
import com.mivuelto.core.toTimeFormatted
import com.mivuelto.feature.checkpayment.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class CheckPaymentViewModel @Inject constructor(
    private val queryTransaction: QueryTransactionUseCase,
    private val observeBanks: ObserveBanksUseCase,
    private val syncBanks: SyncBanksUseCase,
    private val observeUser: ObserveUserUseCase,
) : ViewModel() {

    var state by mutableStateOf(CheckPaymentModel())
        private set

    var errorMsg = ""

    var verifiedTransaction: Transaction? = null
        private set

    var banks by mutableStateOf<List<BankModel>>(emptyList())
        private set

    var merchantRif by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            observeUser().collect { merchantRif = it?.merchantRif }
        }
    }


    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()


    fun updateState(model: CheckPaymentModel) { state = model }

    private var banksLoaded = false

    fun loadBanks() {
        if (banksLoaded) return
        banksLoaded = true
        viewModelScope.launch {
            observeBanks().collect { bankList ->
                banks = bankList
                if (bankList.isEmpty()) {
                    syncBanks().fold(
                        onSuccess = { banks = it },
                        onFailure = { e ->
                            errorMsg = e.message ?: "Unknown error"
                            _effect.send(UiEvent.Error(msg = errorMsg))
                        }
                    )
                }
            }
        }
    }

    fun getInvoice(): InvoiceModel = InvoiceModel(
        ref = verifiedTransaction?.referenceNumber ?: state.reference ?: "empty",
        date = verifiedTransaction?.created?.toDateFormatted() ?: "22/06/2026 6:33 pm",
        time = verifiedTransaction?.created?.toTimeFormatted() ?: "22/06/2026 6:33 pm",
        bank = state.bank ?: BankModel(),
        businessBankRif = merchantRif ?: "J-12345678",
        amount = verifiedTransaction?.let { formatCents(it.amountCents) } ?: state.amount ?: "empty",
        phone = verifiedTransaction?.toPhone ?: state.phone ?: "empty"
    )

    fun sendPayment() {
        viewModelScope.launch {
            queryTransaction(state.toTransactionQuery())
                .fold(
                    onFailure = { e ->
                        log("excep = $e")
                        errorMsg = e.message ?: "Unknown error"
                        _effect.send(UiEvent.Error(msg = errorMsg))
                    },
                    onSuccess = { transaction ->
                        verifiedTransaction = transaction
                        _effect.send(UiEvent.OnSuccess)
                        delay(SUCCESS_VISIBLE_MS)
                        _effect.send(UiEvent.TaskDone())
                    }
                )
        }
    }

    private fun CheckPaymentModel.toTransactionQuery() = TransactionQuery(
        transactionType = TransactionType.PAGO_MOVIL,
        amount = parseAmountCents(amount),
        date = date?.trim()?.takeIf { it.isNotBlank() },
        reference = reference?.trim()?.takeIf { it.isNotBlank() },
        phone = phone?.filter(Char::isDigit)?.takeIf { it.isNotBlank() },
        document = null,
        bankId = bank?.uuid
    )

    private companion object {
        const val SUCCESS_VISIBLE_MS = 1_500L

        fun parseAmountCents(raw: String?): BigDecimal {
            val digits = raw?.filter(Char::isDigit).orEmpty()
            if (digits.isBlank()) return BigDecimal.ZERO
            return BigDecimal(digits).movePointLeft(2)
        }

        fun formatCents(cents: Long): String {
            val units = cents / 100
            val remainder = (cents % 100).toString().padStart(2, '0')
            return "$units,$remainder"
        }
    }
}

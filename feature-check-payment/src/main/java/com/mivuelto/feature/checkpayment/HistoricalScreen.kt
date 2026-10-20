package com.mivuelto.feature.checkpayment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.feature.checkpayment.ui.navigation.HistoricalViewModel

@Composable
fun HistoricalScreen(
    viewModel: HistoricalViewModel,
    onBack: () -> Unit = {},
) {

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    BaseScreen(onBack) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CorpoCreditLogo(modifier = Modifier.padding(top = 10.dp))

            Text(
                text = stringResource(R.string.historical_title),
                style = Lato.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (viewModel.isLoading) {
                Text(
                    text = stringResource(R.string.loading),
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else if (viewModel.transactions.isEmpty() && !viewModel.isLoading) {
                Text(
                    text = stringResource(R.string.no_transactions),
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    items(viewModel.transactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            formatAmount = viewModel::formatAmount,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: com.mivuelto.core.domain.model.Transaction,
    formatAmount: (Long) -> String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = transaction.referenceNumber ?: "Sin referencia",
                style = Lato.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${formatAmount(transaction.amountCents)} Bs.",
                style = Lato.bodyMedium
            )
            Text(
                text = transaction.created,
                style = Lato.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

package com.mivuelto.core.ui.design.selectors

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankSelector(
    modifier: Modifier = Modifier,
    onItemSelected: (Int)->Unit = {},
    banks: List<BankModel>
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedBank by remember { mutableStateOf(banks.first()) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selectedBank.name,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(selectedBank.logo?:R.drawable.ic_anulacion),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            banks.forEachIndexed { i, bank ->

                DropdownMenuItem(
                    onClick = {
                        selectedBank = bank
                        expanded = false
                        onItemSelected(i)
                    },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(bank.logo?:R.drawable.ic_anulacion),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = bank.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = bank.code,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}


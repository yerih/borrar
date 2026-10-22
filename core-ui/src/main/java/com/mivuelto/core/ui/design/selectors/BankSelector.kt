package com.mivuelto.core.ui.design.selectors

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankSelector(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit = {},
    banks: List<BankModel>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBank by remember { mutableStateOf(banks.first()) }
    var menuWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val menuWidth = with(density) { menuWidthPx.toDp() }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedBank.name,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    menuWidthPx = coordinates.size.width
                },
            trailingIcon = {
                // Handles the dropdown arrow icon and its rotation animation
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.ic_bank),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            },
            // Makes the text field clickable to toggle the popup dropdown
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            expanded = !expanded
                        }
                    }
                }
            }
        )

        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    modifier = Modifier
                        .width(menuWidth)
                        .heightIn(max = 480.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(banks) { i, bank ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedBank = bank
                                    expanded = false
                                    onItemSelected(i)
                                },
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(bank.logo ?: R.drawable.ic_anulacion),
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
        }
    }
}



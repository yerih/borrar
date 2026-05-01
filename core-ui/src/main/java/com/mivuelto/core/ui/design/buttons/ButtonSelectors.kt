package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.theme.BluePrimary
import com.mivuelto.core.ui.theme.BlueSecondary
import com.mivuelto.core.ui.theme.BlueSelected
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato


@Composable
fun ButtonSelectors(
    options: List<String> = listOf("Débito", "Crédito", "Visa/Master débito", "Extrafinanciamiento"),
    modifier: Modifier = Modifier,
    onClick: (Int)->Unit = {},
){
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        modifier = modifier.fillMaxWidth().background(Color.Transparent)
            .clip(RoundedCornerShape(12.dp))
            .shadow(20.dp),
        indicator = {p ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(p[selectedTabIndex]),
                color = BlueSecondary
            )
        }
    ) {
        options.forEachIndexed{ i, op ->
            Tab(
                selected = selectedTabIndex == i,
                modifier = Modifier.background(if(selectedTabIndex == i) BluePrimary else BlueSelected)
                    .wrapContentWidth(),
                onClick = { selectedTabIndex = i; onClick(i) }
            ){
                Text(
                    text = op,
                    color = Color.White,
                    modifier = Modifier.padding(15.dp),
                    style = Lato.labelSmall.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }
}



@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonSelectors()
    }
}
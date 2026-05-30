package com.mivuelto.feature.purchase

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.buttons.ButtonHome
import com.mivuelto.core.ui.getVersionName
import com.mivuelto.core.ui.NavFeature
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.CorpoCreditTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFunctionClicked: (NavFeature)->Unit = {},
    onBack: ()->Unit = {},
) {
    BaseScreen (onBack){
        val version = LocalContext.current.getVersionName()

        HeaderAndFooter2(isScrollable = false) {
            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val (menu, versionRef) = createRefs()
                val spaced = 15.dp
                LazyVerticalGrid(
                    modifier = Modifier.constrainAs(menu) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                        bottom.linkTo(versionRef.top)
                    },
                    columns = object : GridCells {
                        override fun Density.calculateCrossAxisCellSizes(
                            availableSize: Int,
                            spacing: Int,
                        ): List<Int> {
                            val v = (availableSize - spacing) / 2
                            return listOf(v, v)
                        }

                    },
                    verticalArrangement = Arrangement.spacedBy(spaced),
                    horizontalArrangement = Arrangement.spacedBy(spaced),
                    contentPadding = PaddingValues(horizontal = spaced),
                ) {
                    items(4) { i ->
                        ButtonHome(
                            textId = getTextId(i),
                            idIcon = getDrawableId(i),
                            modifier = Modifier.height(130.dp),
                        ) {
                            when(i){
                                0 -> onFunctionClicked(NavFeature.CHECK_PAYMENT)
                                1 -> onFunctionClicked(NavFeature.CHANGE)
                                2 -> onFunctionClicked(NavFeature.HISTORICAL)
                                3 -> onFunctionClicked(NavFeature.SETTING)
                            }
                        }
                    }
                }
                Text(
                    text = "Version $version",
                    style = Lato.titleSmall,
                    modifier = Modifier.constrainAs(versionRef){
                        centerHorizontallyTo(parent)
                        top.linkTo(menu.bottom, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                    }
                )
            }
        }
    }
}


private fun getTextId(i: Int): Int {
    return when (i) {
        0 -> R.string.verify_payment
        1 -> R.string.make_change
        2 -> R.string.historical
        3 -> com.mivuelto.core.ui.R.string.setting
        else -> com.mivuelto.core.ui.R.string.enter
    }
}

private fun getDrawableId(i: Int): Int {
    return when (i) {
        0 -> com.mivuelto.core.ui.R.drawable.ic_compra
        1 -> com.mivuelto.core.ui.R.drawable.ic_anulacion
        2 -> com.mivuelto.core.ui.R.drawable.ic_resumen_operaciones
        3 -> com.mivuelto.core.ui.R.drawable.ic_otras_operaciones
        else -> com.mivuelto.core.ui.R.drawable.ic_resumen_operaciones
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        HomeScreen(modifier = Modifier.padding(10.dp))
    }
}



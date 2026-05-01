package com.mivuelto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorpoCreditTheme {
                AppNavigation()
            }
        }
    }
}

package com.example.taazakhabar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.taazakhabar.presentation.components.BottomNavigationBar
import com.example.taazakhabar.presentation.screens.HomeScreen
import com.example.taazakhabar.presentation.screens.TopicsScreen
import com.example.taazakhabar.presentation.ui.theme.TaazaKhabarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaazaKhabarTheme {
                var selectedItem by remember { mutableIntStateOf(0) }
                
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            selectedItem = selectedItem,
                            onItemClick = { selectedItem = it }
                        )
                    }
                ) { innerPadding ->
                    // Use only bottom padding from Scaffold to avoid double status bar height
                    // Screens will handle their own top bar and insets
                    Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                        when (selectedItem) {
                            0 -> HomeScreen(
                                viewModel = newsViewModel
                            )
                            1 -> TopicsScreen(
                                viewModel = newsViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

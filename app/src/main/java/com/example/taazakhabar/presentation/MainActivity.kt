package com.example.taazakhabar.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.presentation.components.BottomNavigationBar
import com.example.taazakhabar.presentation.screens.ArticleDetailScreen
import com.example.taazakhabar.presentation.screens.HomeScreen
import com.example.taazakhabar.presentation.screens.SavedArticlesScreen
import com.example.taazakhabar.presentation.screens.TopicsScreen
import com.example.taazakhabar.presentation.ui.theme.TaazaKhabarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val articleDetailViewModel: ArticleDetailViewModel by viewModels()
    private var keepSplashOn: Boolean = true

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashOn
            }
        }

        lifecycleScope.launch {
            delay(2500)
            keepSplashOn = false
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaazaKhabarTheme {
                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedArticle by remember { mutableStateOf<Article?>(null) }

                BackHandler(enabled = selectedArticle != null) {
                    selectedArticle = null
                }

                if (selectedArticle != null) {
                    ArticleDetailScreen(
                        article = selectedArticle!!,
                        viewModel = articleDetailViewModel,
                        onBackClick = { selectedArticle = null }
                    )
                } else {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                selectedItem = selectedTab,
                                onItemClick = { selectedTab = it }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                            when (selectedTab) {
                                0 -> HomeScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { selectedArticle = it }
                                )
                                1 -> TopicsScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { selectedArticle = it }
                                )
                                2 -> SavedArticlesScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { selectedArticle = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

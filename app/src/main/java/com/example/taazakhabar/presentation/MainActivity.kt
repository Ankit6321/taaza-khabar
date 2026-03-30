package com.example.taazakhabar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.taazakhabar.presentation.components.BottomNavigationBar
import com.example.taazakhabar.presentation.screens.ArticleDetailScreen
import com.example.taazakhabar.presentation.screens.HomeScreen
import com.example.taazakhabar.presentation.screens.SavedArticlesScreen
import com.example.taazakhabar.presentation.screens.TopicsScreen
import com.example.taazakhabar.presentation.ui.theme.TaazaKhabarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val articleDetailViewModel: ArticleDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        lifecycleScope.launch {
            delay(3500)
            keepSplashScreen = false
        }
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        enableEdgeToEdge()

        setContent {
            TaazaKhabarTheme {

                val selectedTab by newsViewModel.selectedTab.collectAsStateWithLifecycle()
                val selectedArticle by newsViewModel.selectedArticle.collectAsStateWithLifecycle()

                BackHandler(enabled = selectedArticle != null) {
                    newsViewModel.onArticleClicked(null)
                }

                if (selectedArticle != null) {
                    ArticleDetailScreen(
                        article = selectedArticle!!,
                        viewModel = articleDetailViewModel,
                        onBackClick = { newsViewModel.onArticleClicked(null) }
                    )
                } else {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                selectedItem = selectedTab,
                                onItemClick = { newsViewModel.onTabSelected(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                            when (selectedTab) {
                                0 -> HomeScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { newsViewModel.onArticleClicked(it) }
                                )

                                1 -> TopicsScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { newsViewModel.onArticleClicked(it) }
                                )

                                2 -> SavedArticlesScreen(
                                    viewModel = newsViewModel,
                                    onArticleClick = { newsViewModel.onArticleClicked(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

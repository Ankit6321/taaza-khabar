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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.presentation.components.BottomNavigationBar
import com.example.taazakhabar.presentation.screens.ArticleDetailScreen
import com.example.taazakhabar.presentation.screens.HomeScreen
import com.example.taazakhabar.presentation.screens.SavedArticlesScreen
import com.example.taazakhabar.presentation.screens.TopicsScreen
import com.example.taazakhabar.presentation.ui.theme.TaazaKhabarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val articleDetailViewModel: ArticleDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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

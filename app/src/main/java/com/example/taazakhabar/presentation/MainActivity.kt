package com.example.taazakhabar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.presentation.ui.theme.TaazaKhabarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val newsViewModel: NewsViewModel by viewModels()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaazaKhabarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = newsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NewScreen(modifier: Modifier = Modifier, viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        state.value.onSuccess {
            items(it) { news ->
                NewsItem(news = news)
            }
        }
    }
}

@Composable
fun NewsItem(modifier: Modifier = Modifier, news: News) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = news.imageBaseUrl,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun NewsItemPreview() {
    TaazaKhabarTheme {
        NewsItem(
            news = News(
                imageBaseUrl = "https://media.inshorts.com/inshorts/images/v1/variants/jpg/m/2026/03_mar/13_fri/img_1773410579275_528.jpg",
                category = listOf("National", "World"),
                title = "l;adsfjlkasjlkj",
                content = "adjfskalsdk",
                author = "lfakjsdf",
            )
        )
    }
}
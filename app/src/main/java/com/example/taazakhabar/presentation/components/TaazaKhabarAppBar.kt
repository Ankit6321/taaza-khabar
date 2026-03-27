package com.example.taazakhabar.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.HttpException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaazaKhabarAppBar(
    title: String = "TaazaKhabar",
    error: Throwable? = null,
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isError = error != null && !isRefreshing

    Column(modifier = modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        AnimatedVisibility(visible = isError) {
            val (message, icon) = when (error) {
                is HttpException -> if (error.code() == 429) {
                    "Rate limited - please wait" to Icons.Default.Info
                } else {
                    "Server error - showing cache" to Icons.Default.Info
                }
                is UnknownHostException -> "No internet connection" to Icons.Default.WifiOff
                else -> "Showing cached data" to Icons.Default.Info
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

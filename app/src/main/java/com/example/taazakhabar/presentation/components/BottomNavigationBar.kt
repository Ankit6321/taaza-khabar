package com.example.taazakhabar.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<NavigationItem> = listOf(
        NavigationItem("Home", Icons.Default.Home, Icons.Default.Home),
        NavigationItem("Topics", Icons.Default.Topic, Icons.Default.Topic),
        NavigationItem("Saved", Icons.Default.Bookmark, Icons.Default.Bookmark)
    ),
    selectedItem: Int = 0,
    onItemClick: (Int) -> Unit = {}
) {
    NavigationBar(modifier = modifier) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemClick(index) },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

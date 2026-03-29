package com.example.taazakhabar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taazakhabar.data.local.dao.CachedEntertainmentArticleDao
import com.example.taazakhabar.data.local.dao.CachedScienceArticleDao
import com.example.taazakhabar.data.local.dao.CachedSportsArticleDao
import com.example.taazakhabar.data.local.dao.CachedTechnologyArticleDao
import com.example.taazakhabar.data.local.dao.CachedTopArticleDao
import com.example.taazakhabar.data.local.dao.CachedTrendingArticleDao
import com.example.taazakhabar.data.local.dao.SavedArticleDao
import com.example.taazakhabar.data.local.entities.CachedEntertainmentArticleEntity
import com.example.taazakhabar.data.local.entities.CachedScienceArticleEntity
import com.example.taazakhabar.data.local.entities.CachedSportsArticleEntity
import com.example.taazakhabar.data.local.entities.CachedTechnologyArticleEntity
import com.example.taazakhabar.data.local.entities.CachedTopArticleEntity
import com.example.taazakhabar.data.local.entities.CachedTrendingArticleEntity
import com.example.taazakhabar.data.local.entities.SavedArticleEntity

@Database(
    entities = [
        SavedArticleEntity::class,
        CachedTopArticleEntity::class,
        CachedSportsArticleEntity::class,
        CachedScienceArticleEntity::class,
        CachedTechnologyArticleEntity::class,
        CachedEntertainmentArticleEntity::class,
        CachedTrendingArticleEntity::class
    ],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val savedArticleDao: SavedArticleDao
    abstract val cachedTopArticleDao: CachedTopArticleDao
    abstract val cachedSportsArticleDao: CachedSportsArticleDao
    abstract val cachedScienceArticleDao: CachedScienceArticleDao
    abstract val cachedTechnologyArticleDao: CachedTechnologyArticleDao
    abstract val cachedEntertainmentArticleDao: CachedEntertainmentArticleDao
    abstract val cachedTrendingArticleDao: CachedTrendingArticleDao

}
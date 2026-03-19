package com.example.taazakhabar.di

import com.example.taazakhabar.data.local.ArticleDatabase
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.data.repository.NewsRepositoryImpl
import com.example.taazakhabar.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        apiService: ApiService,
        articleDatabase: ArticleDatabase
    ): NewsRepository {
        return NewsRepositoryImpl(
            apiService,
            articleDatabase
        )
    }
}
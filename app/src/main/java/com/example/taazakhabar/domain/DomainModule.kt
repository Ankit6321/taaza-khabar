package com.example.taazakhabar.domain

import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.data.repository.NewsRepositoryImpl
import com.example.taazakhabar.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    fun provideRepository(apiService: ApiService): NewsRepository {
        return NewsRepositoryImpl(apiService)
    }
}
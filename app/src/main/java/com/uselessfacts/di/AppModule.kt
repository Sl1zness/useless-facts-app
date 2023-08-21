package com.uselessfacts.di

import com.uselessfacts.model.UselessFactsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): UselessFactsApi {
        return Retrofit.Builder().baseUrl("https://uselessfacts.jsph.pl")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(UselessFactsApi::class.java)
    }
}
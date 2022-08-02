package com.jerimkaura.contacts.di

import com.jerimkaura.contacts.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideContactsDao(database: AppDatabase) = database.getContactDao()
}
package com.jerimkaura.contacts.di

import android.content.Context
import androidx.room.Room
import com.jerimkaura.contacts.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "contacts_database"
    ).allowMainThreadQueries().build()
}
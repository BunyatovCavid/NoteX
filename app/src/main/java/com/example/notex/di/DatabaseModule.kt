package com.example.notex.di

import android.content.Context
import androidx.room.Room
import com.example.notex.data.repositories.Database.AppDatabase
import com.example.notex.data.repositories.Database.Dao.LoginDao
import com.example.notex.data.repositories.Database.Dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.Name,
        ).build()
    }

    @Provides
    fun provideLoginDao(db: AppDatabase): LoginDao {
        return db.loginDao()
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }


}

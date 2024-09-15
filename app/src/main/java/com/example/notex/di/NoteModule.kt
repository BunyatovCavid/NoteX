package com.example.notex.di

import com.example.notex.data.Database.Dao.LoginDao
import com.example.notex.data.Database.Dao.NoteDao
import com.example.notex.data.interfaces.authorizationInterface
import com.example.notex.data.interfaces.noteInteface
import com.example.notex.data.repositories.authorizationRepository
import com.example.notex.data.repositories.noteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {


    @Provides
    @Singleton
    fun getAuthRepository(loginDao: LoginDao): authorizationInterface {
        return authorizationRepository(loginDao)
    }

    @Provides
    @Singleton
    fun getNoteRepository(noteDao: NoteDao): noteInteface {
        return noteRepository(noteDao)
    }



}
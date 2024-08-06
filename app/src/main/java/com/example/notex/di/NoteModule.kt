package com.example.notex.di

import com.example.notex.data.interfaces.authorizationInterface
import com.example.notex.data.repositories.authorizationRepository
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
    fun getAuth():authorizationInterface=
         authorizationRepository()


}
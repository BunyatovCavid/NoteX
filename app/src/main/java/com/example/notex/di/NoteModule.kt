package com.example.notex.di

import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.Dao.NoteDao
import com.example.notex.data.interfaces.AuthorizationInterface
import com.example.notex.data.interfaces.CategorieInterface
import com.example.notex.data.interfaces.NoteInteface
import com.example.notex.data.interfaces.SpecialNotesInterface
import com.example.notex.data.interfaces.UserInterface
import com.example.notex.data.repositories.AuthorizationRepository
import com.example.notex.data.repositories.CategorieRepository
import com.example.notex.data.repositories.NoteRepository
import com.example.notex.data.repositories.SpeacialNoteRepository
import com.example.notex.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
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
    fun getspecialNoteRepository(): SpecialNotesInterface {
        return SpeacialNoteRepository()
    }


    @Provides
    @Singleton
    fun getAuthRepository(loginDao: LoginDao): AuthorizationInterface {
        return AuthorizationRepository(loginDao)
    }

    @Provides
    @Singleton
    fun getNoteRepository(noteDao: NoteDao): NoteInteface {
        return NoteRepository(noteDao)
    }


    @Provides
    @Singleton
    fun getCategoryRepository(): CategorieInterface {
        return CategorieRepository()
    }


    @Provides
    @Singleton
    fun getUserRepository(): UserInterface {
        return UserRepository()
    }



}
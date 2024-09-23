package com.example.notex

import android.app.Application
import androidx.room.Room
import com.example.notex.data.repositories.Database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp: Application() {
}



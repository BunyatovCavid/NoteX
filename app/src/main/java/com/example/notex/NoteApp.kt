package com.example.notex

import android.app.Application
import androidx.room.Room
import com.example.notex.data.repositories.Database.AppDatabase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }

}



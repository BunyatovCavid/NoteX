package com.example.notex.data.repositories.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notex.data.models.LoginEntity
import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.Dao.NoteDao
import com.example.notex.data.models.Note

@Database(entities = [LoginEntity::class, Note::class], version = 1, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
    abstract fun noteDao(): NoteDao

    companion object{
        const val Name = "notex-database"
    }
}

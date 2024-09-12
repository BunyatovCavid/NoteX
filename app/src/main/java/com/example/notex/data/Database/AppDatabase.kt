package com.example.notex.data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notex.data.Entities.LoginEntity
import com.example.notex.data.Database.Dao.LoginDao

@Database(entities = [LoginEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object{
        const val Name = "login-database"
    }
}

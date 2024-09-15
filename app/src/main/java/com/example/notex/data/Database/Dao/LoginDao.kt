package com.example.notex.data.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notex.data.models.LoginEntity

@Dao
interface LoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoginData(loginEntity: LoginEntity)

    @Query("SELECT * FROM login_data LIMIT 1")
    suspend fun getLoginData(): LoginEntity?
}

package com.example.notex.data.interfaces

import android.content.Context
import com.example.notex.data.models.LoginEntity
import com.google.firebase.auth.FirebaseAuth

interface AuthorizationInterface {


    fun logIn(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singUp(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singOut(context: Context)

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit)

    suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity)

}

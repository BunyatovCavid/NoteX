package com.example.notex.data.interfaces

import com.example.notex.data.models.LoginEntity
import com.google.firebase.auth.FirebaseAuth

interface AuthorizationInterface {


    fun logIn(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singUp(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singOut()

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit)

    suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity)

}

package com.example.notex.data.interfaces

import androidx.fragment.app.Fragment
import com.example.notex.data.Entities.LoginEntity
import com.google.firebase.auth.FirebaseAuth

interface authorizationInterface {
   var firebaseAuth:FirebaseAuth

    fun logIn(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singUp(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit)

    suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity)

}

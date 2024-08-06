package com.example.notex.data.interfaces

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

interface authorizationInterface {
   var firebaseAuth:FirebaseAuth

    fun logIn(email:String, password:String,f:Fragment)

    fun singUp(email:String, password:String,f:Fragment)

    fun resetPassword(email:String)
}
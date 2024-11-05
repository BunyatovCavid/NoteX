package com.example.notex.data.interfaces

import android.content.Context

interface AuthorizationInterface {


    fun logIn(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singUp(email:String, password:String, callback: (Boolean, String?) -> Unit)

    fun singOut(context: Context)

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit)


}

package com.example.notex.data.repositories

import android.net.Uri
import android.util.Log
import com.example.notex.data.interfaces.UserInterface
import com.example.notex.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.crashlytics.FirebaseCrashlytics

class UserRepository: UserInterface {


    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private  val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    val user = firebaseAuth.currentUser
    override fun getUser():UserModel{
        var response = UserModel()
        response.imageUrl = user?.photoUrl.toString()
        response.name = user?.displayName
        response.email = user?.email

        return response
    }

    override fun updateUser(userModel: UserModel, callback: (String) -> Unit?) {
        val profileUpdatesBuilder = UserProfileChangeRequest.Builder()

        if (userModel.name != null)
            profileUpdatesBuilder.setDisplayName(userModel.name)
        if (userModel.imageUrl != null)
            profileUpdatesBuilder.setPhotoUri(Uri.parse(userModel.imageUrl))

        val profileUpdates = profileUpdatesBuilder.build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback("Success")
                    Log.d("ProfileUpdate", "User profile updated.")
                } else {
                    crashlytics.log(task.exception?.message.toString())
                    callback("The process failed")
                }
            }
            ?.addOnFailureListener { e ->
                crashlytics.recordException(e)
                callback("The process failed")
                Log.d("CostumeExceptionHandle", e.message.toString())
            }

    }
    override fun changePassword(password: String,callback: (String)->Unit?) {

        if (password.length < 6) {
           callback ("Password length must be at least 6 symbols")
        }
        else if (!password.any { it.isDigit() }) {
            callback ("Password must contain numbers")
        }
        else if (!password.any { it.isUpperCase() }) {
            callback ("Password must contain uppercase letters")
        }
        else {
            user?.updatePassword(password)
                ?.addOnSuccessListener { callback("Success")}
                ?.addOnFailureListener{task->  crashlytics.recordException(task)}
        }

    }

}
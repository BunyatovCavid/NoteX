package com.example.notex.data.repositories

import android.net.Uri
import android.util.Log
import com.example.notex.data.interfaces.UserInterface
import com.example.notex.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class UserRepository: UserInterface {

    val user = FirebaseAuth.getInstance().currentUser
    override fun getUser():UserModel{
        var response = UserModel()
        response.imageUrl = user?.photoUrl.toString()
        response.name = user?.displayName
        response.email = user?.email

        return response
    }


    override fun updateUser(userModel: UserModel, callback: (String) -> Unit?) {
        val profileUpdatesBuilder = UserProfileChangeRequest.Builder()

        if(userModel.name!=null)
            profileUpdatesBuilder.setDisplayName(userModel.name)
        if(userModel.imageUrl!=null)
            profileUpdatesBuilder.setPhotoUri(Uri.parse(userModel.imageUrl))

        var profileUpdates = profileUpdatesBuilder.build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                    callback("Success")
                    Log.d("ProfileUpdate", "User profile updated.")
            }
            ?.addOnFailureListener{task-> callback(task.toString())}

        if (userModel.email!=null) {
            user?.updateEmail(userModel.email.toString())
                ?.addOnSuccessListener {callback("Success")}
                ?.addOnFailureListener {task-> callback(task.toString())}

        }

    }

    override fun changePassword(password: String,callback: (String)->Unit?) {
        user?.updatePassword(password)
            ?.addOnSuccessListener { callback("Success")}
            ?.addOnFailureListener{task-> callback(task.toString())}
    }

}
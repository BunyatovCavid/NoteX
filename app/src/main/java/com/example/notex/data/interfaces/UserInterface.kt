package com.example.notex.data.interfaces

import com.example.notex.data.models.UserModel

interface UserInterface {
    fun getUser():UserModel
    fun updateUser(userModel: UserModel, callback: (String) -> Unit?)
    fun changePassword(password:String, callback: (String) -> Unit?)
}
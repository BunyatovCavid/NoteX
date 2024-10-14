package com.example.notex.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var name:String? = null,
    var imageUrl: String? = null,
    var email:String?=null
):Parcelable

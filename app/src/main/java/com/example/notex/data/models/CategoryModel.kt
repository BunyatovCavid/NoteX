package com.example.notex.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryModel (
    var id:String ="",
    var title: String = "",
    var field1: specialField? = null,
    var field2: specialField? = null,
    var field3: specialField? = null,
    var field4: specialField? = null,
    var field5: specialField? = null,
    var field6: specialField? = null,
    var field7: specialField? = null,
    var userId: String = ""):Parcelable

@Parcelize
data class specialField(
    var title: String = "",
    var datatype: String = "",
): Parcelable